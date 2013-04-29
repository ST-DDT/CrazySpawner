package de.st_ddt.crazyspawner.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.data.meta.NameMeta;
import de.st_ddt.crazyspawner.data.meta.PeacefulMeta;
import de.st_ddt.crazyspawner.tasks.HealthTask;

public class CreatureListener implements Listener
{

	private final CrazySpawner plugin;
	private final HealthTask health;

	public CreatureListener(final CrazySpawner plugin)
	{
		super();
		this.plugin = plugin;
		this.health = new HealthTask(plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void CreatureDamage(final EntityDamageEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		final LivingEntity entity = (LivingEntity) event.getEntity();
		entity.removeMetadata(PeacefulMeta.METAHEADER, plugin);
		final double alarmRange = plugin.getDefaultAlarmRange();
		final Location location = entity.getLocation();
		for (final LivingEntity nearby : entity.getWorld().getEntitiesByClass(LivingEntity.class))
			if (location.distance(nearby.getLocation()) < alarmRange)
				nearby.removeMetadata(PeacefulMeta.METAHEADER, plugin);
		health.queue(entity);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void EntityTargetEvent(final EntityTargetEvent event)
	{
		if (event.getTarget() == null)
			return;
		if (event.getEntity().hasMetadata(PeacefulMeta.METAHEADER))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void CreatureDeath(final EntityDeathEvent event)
	{
		final LivingEntity entity = event.getEntity();
		final List<MetadataValue> metas = entity.getMetadata("CreatureMeta");
		for (final MetadataValue meta : metas)
			if (meta.getOwningPlugin() == plugin)
			{
				final NameMeta name = (NameMeta) meta;
				entity.setCustomName(name.asString());
			}
	}
}
