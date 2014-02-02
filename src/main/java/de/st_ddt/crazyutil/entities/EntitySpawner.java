package de.st_ddt.crazyutil.entities;

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
	 * @return The highest entity Class shared with all possible spawned entities.<br>
	 *         If this Spawns only one {@link EntityType} this method returns {@link EntityType#getEntityClass()}.
	 */
	public Class<? extends Entity> getEntityClass();

	public EntitySpawnerType getSpawnerType();

	public Entity spawn(Location location);

	@Override
	public boolean matches(Entity entity);
}
