package de.st_ddt.crazyspawner.entities.spawners;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyutil.entities.EntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.ParentedEntitySpawner;

public abstract class BasicParentedSpawner extends BasicSpawner implements ParentedEntitySpawner
{

	protected final EntitySpawner spawner;

	public BasicParentedSpawner(final EntityType type)
	{
		super();
		this.spawner = EntitySpawner.ENTITYSPAWNERS[type.ordinal()];
		if (spawner == null)
			throw new IllegalArgumentException("EntityType " + type.name() + " is not supported!");
	}

	public BasicParentedSpawner(final EntitySpawner spawner)
	{
		super();
		if (spawner == null)
			throw new IllegalArgumentException("Spawner cannot be Null!");
		this.spawner = spawner;
	}

	protected BasicParentedSpawner(final ConfigurationSection config)
	{
		super();
		final ConfigurationSection parentConfig = config.getConfigurationSection("parent");
		if (parentConfig == null)
			this.spawner = EntitySpawnerHelper.loadLegacySpawner(config);
		else
			this.spawner = EntitySpawnerHelper.loadParent(parentConfig);
		if (spawner == null)
			throw new IllegalArgumentException("Spawner cannot be Null!");
	}

	@Override
	public final EntityType getEntityType()
	{
		return spawner.getEntityType();
	}

	@Override
	public final Set<Class<?>> getEntityClasses()
	{
		return spawner.getEntityClasses();
	}

	@Override
	public Entity spawn(final Location location)
	{
		return spawner.spawn(location);
	}

	@Override
	public boolean matches(final Entity entity)
	{
		return spawner.matches(entity);
	}

	@Override
	public final EntitySpawner getParentSpawner()
	{
		return spawner;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		EntitySpawnerHelper.saveParentSpawner(spawner, config, path + "parent.");
	}
}
