package de.st_ddt.crazyutil.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public interface EntitySpawner extends EntityMatcher
{

	public final static EntitySpawner[] ENTITYSPAWNERS = new EntitySpawner[EntityType.values().length];

	public EntityType getType();

	public Class<? extends Entity> getEntityClass();

	public Entity spawn(Location location);

	@Override
	public boolean matches(Entity entity);
}
