package de.st_ddt.crazyspawner.entities.spawners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.persistance.PersistanceManager;
import de.st_ddt.crazyspawner.entities.persistance.PersistantState;
import de.st_ddt.crazyutil.entities.ChangeableNamedEntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;
import de.st_ddt.crazyutil.entities.NamedApplyableEntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.entities.ShowableEntitySpawner;
import de.st_ddt.crazyutil.source.Localized;

public class NamedParentedSpawner extends BasicParentedSpawner implements ChangeableNamedEntitySpawner, NamedApplyableEntitySpawner, ShowableEntitySpawner, MetadataValue, PersistantState
{

	public final static String METAHEADER = "CustomEntityMeta";
	public final static String PERSISTENCEKEY = "SPAWNER";
	protected final String name;

	public NamedParentedSpawner(final EntitySpawner spawner, final String name)
	{
		super(spawner);
		if (name == null)
			throw new IllegalArgumentException("Spawner's name cannot be Null!");
		this.name = name;
	}

	public NamedParentedSpawner(final ConfigurationSection config)
	{
		super(config);
		this.name = config.getString("name", config.getName());
		if (name == null)
			throw new IllegalArgumentException("Spawner's name cannot be Null!");
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "name", name);
		super.save(config, path);
	}

	@Override
	public boolean matches(final Entity entity)
	{
		for (final MetadataValue meta : entity.getMetadata(METAHEADER))
			if (equals(meta))
				return true;
		return false;
	}

	@Override
	public EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.NAMED;
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.SHOW.NAMEDPARENTED {Name} {EntityType}", "CRAZYSPAWNER.ENTITY.SHOW.PARENT", "CRAZYSPAWNER.ENTITY.SHOW.PARENT.NAMED {ParentName}", "CRAZYSPAWNER.ENTITY.SHOW.PARENT.UNKNOWN {ToString}" })
	public void show(final CommandSender target)
	{
		final CrazySpawner plugin = CrazySpawner.getPlugin();
		plugin.sendLocaleMessage("ENTITY.SHOW.NAMEDPARENTED", target, name, EntitySpawnerHelper.getNiceEntityTypeName(getEntityType()));
		plugin.sendLocaleMessage("ENTITY.SHOW.PARENT", target);
		if (spawner instanceof ShowableEntitySpawner)
			((ShowableEntitySpawner) spawner).show(target);
		else if (spawner instanceof NamedEntitySpawner)
			plugin.sendLocaleMessage("ENTITY.SHOW.PARENT.NAMED", target, ((NamedEntitySpawner) spawner).getName());
		else
			plugin.sendLocaleMessage("ENTITY.SHOW.PARENT.UNKNOWN", target, spawner.toString());
	}

	@Override
	public final NamedParentedSpawner value()
	{
		return this;
	}

	@Override
	public final int asInt()
	{
		return 0;
	}

	@Override
	public final float asFloat()
	{
		return 0;
	}

	@Override
	public final double asDouble()
	{
		return 0;
	}

	@Override
	public final long asLong()
	{
		return 0;
	}

	@Override
	public final short asShort()
	{
		return 0;
	}

	@Override
	public final byte asByte()
	{
		return 0;
	}

	@Override
	public final boolean asBoolean()
	{
		return false;
	}

	@Override
	public final String asString()
	{
		return toString();
	}

	@Override
	public final CrazySpawner getOwningPlugin()
	{
		return CrazySpawner.getPlugin();
	}

	@Override
	public final void invalidate()
	{
	}

	@Override
	public String toString()
	{
		return "CustomizedEntitySpawner{name: " + getName() + "; parent: " + spawner.toString() + "}";
	}

	public static NamedParentedSpawner deserialize(final Map<String, Object> map)
	{
		return CrazySpawner.getPlugin().getCustomEntities().get(map.get("name"));
	}

	@Override
	public Map<String, Object> serialize()
	{
		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("name", name);
		return res;
	}

	@Override
	public void attachTo(final Entity entity, final PersistanceManager manager)
	{
		entity.setMetadata(METAHEADER, this);
		manager.watch(entity, PERSISTENCEKEY, this);
	}

	@Override
	public NamedParentedSpawner clone(final String newName)
	{
		return new NamedParentedSpawner(spawner, newName);
	}
}
