package de.st_ddt.crazyspawner.entities.spawners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyutil.ClassUtil;
import de.st_ddt.crazyutil.RandomUtil;
import de.st_ddt.crazyutil.entities.ApplyableEntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;

public class RandomParentedSpawner extends BasicSpawner implements ApplyableEntitySpawner
{

	private final List<EntitySpawner> spawners = new ArrayList<EntitySpawner>();
	private final EntityType type;
	private final Set<Class<?>> entityClasses;

	@SuppressWarnings("unchecked")
	private static Set<Class<?>> getSharedClasses(final Collection<? extends EntitySpawner> spawners)
	{
		final List<Set<Class<?>>> classes = new ArrayList<Set<Class<?>>>(spawners.size());
		for (final EntitySpawner spawner : spawners)
			classes.add(spawner.getEntityClasses());
		return ClassUtil.getSharedClasses(classes.toArray(new Set[spawners.size()]));
	}

	private static EntityType getSharedEntityType(final Collection<? extends EntitySpawner> spawners)
	{
		EntityType type = null;
		for (final EntitySpawner spawner : spawners)
			if (spawner.getEntityType() == null)
				return null;
			else if (type == null)
				type = spawner.getEntityType();
			else if (type != spawner.getEntityType())
				return null;
		return type;
	}

	public RandomParentedSpawner(final EntitySpawner... spawners)
	{
		super();
		for (final EntitySpawner spawner : spawners)
			this.spawners.add(spawner);
		if (this.spawners.isEmpty())
			throw new IllegalArgumentException("Cannot create MultiParentedSpawner without any parents!");
		this.type = getSharedEntityType(this.spawners);
		this.entityClasses = getSharedClasses(this.spawners);
	}

	public RandomParentedSpawner(final Collection<? extends EntitySpawner> spawners)
	{
		super();
		this.spawners.addAll(spawners);
		if (this.spawners.isEmpty())
			throw new IllegalArgumentException("Cannot create MultiParentedSpawner without any parents!");
		this.type = getSharedEntityType(spawners);
		this.entityClasses = getSharedClasses(spawners);
	}

	public RandomParentedSpawner(final ConfigurationSection config)
	{
		super();
		final ConfigurationSection parentsSection = config.getConfigurationSection("parents");
		if (parentsSection != null)
			for (final String key : parentsSection.getKeys(false))
				this.spawners.add(EntitySpawnerHelper.loadParent(parentsSection.getConfigurationSection(key)));
		if (this.spawners.isEmpty())
			throw new IllegalArgumentException("Cannot create MultiParentedSpawner without any parents!");
		this.type = getSharedEntityType(spawners);
		this.entityClasses = getSharedClasses(spawners);
	}

	@Override
	public EntityType getEntityType()
	{
		return type;
	}

	@Override
	public Set<Class<?>> getEntityClasses()
	{
		return entityClasses;
	}

	@Override
	public final EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.RANDOM;
	}

	@Override
	public void apply(final Entity entity)
	{
		final EntitySpawner spawner = randomSpawner();
		if (spawner instanceof ApplyableEntitySpawner)
			((ApplyableEntitySpawner) spawner).apply(entity);
	}

	@Override
	public Entity spawn(final Location location)
	{
		return randomSpawner().spawn(location);
	}

	protected EntitySpawner randomSpawner()
	{
		return RandomUtil.randomElement(spawners);
	}

	@Override
	public boolean matches(final Entity entity)
	{
		for (final EntitySpawner spawner : spawners)
			if (spawner.matches(entity))
				return true;
		return false;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "parents", null);
		int i = 0;
		for (final EntitySpawner spawner : spawners)
			EntitySpawnerHelper.saveParentSpawner(spawner, config, path + "parents." + (i++) + ".");
	}
}
