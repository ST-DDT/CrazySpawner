package de.st_ddt.crazyspawner.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CompatibilityHelper;
import de.st_ddt.crazyspawner.entities.CustomEntitySpawner;
import de.st_ddt.crazyspawner.entities.meta.AlarmMeta;
import de.st_ddt.crazyspawner.entities.meta.CustomDamage;
import de.st_ddt.crazyspawner.entities.meta.CustomDrops;
import de.st_ddt.crazyspawner.entities.meta.CustomXP;
import de.st_ddt.crazyspawner.entities.meta.DetectionMeta;
import de.st_ddt.crazyspawner.entities.meta.FallingBlockMeta;
import de.st_ddt.crazyspawner.entities.meta.NameMeta;
import de.st_ddt.crazyspawner.entities.meta.PeacefulMeta;
import de.st_ddt.crazyspawner.entities.properties.InvulnerableProperty;
import de.st_ddt.crazyspawner.tasks.HealthTask;

public class EntityListener implements Listener
{

	private final CrazySpawner plugin;
	private final HealthTask health;
	private final CustomEntitySpawner[] overwriteEntities;

	public EntityListener(final CrazySpawner plugin, final CustomEntitySpawner[] overwriteEntities)
	{
		super();
		this.plugin = plugin;
		this.health = new HealthTask(plugin);
		this.overwriteEntities = overwriteEntities;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void CreatureSpawn(final CreatureSpawnEvent event)
	{
		final LivingEntity living = event.getEntity();
		final CustomEntitySpawner spawner = overwriteEntities[living.getType().ordinal()];
		if (spawner == null)
			return;
		else
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{

				@Override
				public void run()
				{
					if (!living.isValid())
						return;
					else if (living.hasMetadata(CustomEntitySpawner.METAHEADER))
						return;
					else
						spawner.apply(living);
				}
			});
	}

	@EventHandler(ignoreCancelled = true)
	public void CreatureExplosion(final EntityDamageByEntityEvent event)
	{
		if (plugin.isMonsterExplosionDamageEnabled() || event.getCause() != DamageCause.ENTITY_EXPLOSION)
			return;
		if (!(event.getEntity() instanceof Monster))
			return;
		if (event.getDamager() instanceof TNTPrimed)
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void EntityDamaged(final EntityDamageEvent event)
	{
		if (event.getEntity().hasMetadata(InvulnerableProperty.METAHEADER))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void CreatureDamaged(final EntityDamageEvent event)
	{
		if (event.getEntity() instanceof LivingEntity)
			health.queue((LivingEntity) event.getEntity());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void CreatureDamagedbyEntity(final EntityDamageByEntityEvent event)
	{
		final Entity entity = event.getEntity();
		entity.removeMetadata(DetectionMeta.METAHEADER, plugin);
		double alarmRange = plugin.getDefaultAlarmRange();
		final List<MetadataValue> metas = entity.getMetadata(AlarmMeta.METAHEADER);
		for (final MetadataValue meta : metas)
			if (meta instanceof AlarmMeta)
			{
				final AlarmMeta alarm = (AlarmMeta) meta;
				alarmRange = alarm.getAlarmRange();
				break;
			}
		final Location location = entity.getLocation();
		for (final LivingEntity nearby : entity.getWorld().getEntitiesByClass(LivingEntity.class))
			if (location.distance(nearby.getLocation()) < alarmRange)
				nearby.removeMetadata(DetectionMeta.METAHEADER, plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void CreatureDamager(final EntityDamageByEntityEvent event)
	{
		final Entity entity = event.getDamager();
		final List<MetadataValue> damageMetas = entity.getMetadata(CustomDamage.METAHEADER);
		for (final MetadataValue meta : damageMetas)
			if (meta instanceof CustomDamage)
			{
				final CustomDamage damage = (CustomDamage) meta;
				CompatibilityHelper.setDamage(event, damage.getDamage());
				break;
			}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void EntityTargetEvent(final EntityTargetEvent event)
	{
		final Entity target = event.getTarget();
		if (target == null)
			return;
		final Entity entity = event.getEntity();
		if (!(entity instanceof LivingEntity))
			return;
		final LivingEntity living = (LivingEntity) entity;
		if (living.hasMetadata(PeacefulMeta.METAHEADER))
			event.setCancelled(true);
		else
		{
			if (!living.getWorld().equals(target.getWorld()))
				return;
			final List<MetadataValue> detectionMeta = living.getMetadata(DetectionMeta.METAHEADER);
			for (final MetadataValue meta : detectionMeta)
				if (meta instanceof DetectionMeta)
				{
					final DetectionMeta detection = (DetectionMeta) meta;
					if (!detection.canDetect(living, target))
						event.setCancelled(true);
					break;
				}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void FallingBlockImpact(final EntityChangeBlockEvent event)
	{
		final Entity entity = event.getEntity();
		if (!(entity instanceof FallingBlock))
			return;
		final FallingBlock falling = (FallingBlock) entity;
		final List<MetadataValue> fallingBlockMeta = falling.getMetadata(FallingBlockMeta.METAHEADER);
		for (final MetadataValue meta : fallingBlockMeta)
			if (meta instanceof FallingBlockMeta)
			{
				final FallingBlockMeta fallingMeta = (FallingBlockMeta) meta;
				event.setCancelled(true);
				if (fallingMeta.isDespawningOnImpactEnabled())
					break;
				Material material;
				if (fallingMeta.getPlacedMaterial() == null)
					material = falling.getMaterial();
				else
					material = fallingMeta.getPlacedMaterial();
				byte data;
				if (fallingMeta.getPlacedMaterialData() == null)
					data = falling.getBlockData();
				else
					data = fallingMeta.getPlacedMaterialData();
				final Block block = event.getBlock();
				block.setTypeIdAndData(material.getId(), data, true);
				fallingMeta.apply(block);
				break;
			}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void CreatureDeath(final EntityDeathEvent event)
	{
		final LivingEntity entity = event.getEntity();
		final List<MetadataValue> nameMetas = entity.getMetadata(NameMeta.METAHEADER);
		for (final MetadataValue meta : nameMetas)
			if (meta.getOwningPlugin() == plugin)
			{
				final NameMeta name = (NameMeta) meta;
				entity.setCustomName(name.getCustomName());
				break;
			}
		final List<MetadataValue> dropsMeta = entity.getMetadata(CustomDrops.METAHEADER);
		for (final MetadataValue meta : dropsMeta)
			if (meta instanceof CustomDrops)
			{
				((CustomDrops) meta).updateDrops(event.getDrops());
				break;
			}
		final List<MetadataValue> xpMeta = entity.getMetadata(CustomXP.METAHEADER);
		for (final MetadataValue meta : xpMeta)
			if (meta instanceof CustomXP)
			{
				event.setDroppedExp(((CustomXP) meta).getXP());
				break;
			}
	}
}
