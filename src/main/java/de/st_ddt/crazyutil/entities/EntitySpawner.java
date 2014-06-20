package de.st_ddt.crazyutil.entities;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Classes implementing this Interface are Entity Spawners.
 */
public interface EntitySpawner extends EntityMatcher
{

	public final static EntitySpawner[] ENTITYSPAWNERS = new EntitySpawner[EntityType.values().length];

	/**
	 * @return The {@link EntityType} spawned by this Spawner.<br>
	 *         If it does spawn more than one {@link EntityType}, this method return Null.
	 */
	public EntityType getEntityType();

	/**
	 * @return An immutable set containing all Classes that are supported by all possible spawned entities.
	 */
	public Set<Class<?>> getEntityClasses();

	public EntitySpawnerType getSpawnerType();

	public Entity spawn(Location location);

	@Override
	public boolean matches(Entity entity);
}
