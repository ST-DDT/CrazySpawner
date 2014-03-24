package de.st_ddt.crazyspawner.craftbukkit.v1_7_R2.entities.spawners;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.st_ddt.crazyspawner.craftbukkit.v1_7_R2.entities.spawners.players.NPCHelper;
import de.st_ddt.crazyutil.NamesHelper;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;

public final class RandomPlayerSpawner implements NamedEntitySpawner
{

	public RandomPlayerSpawner()
	{
		super();
	}

	@Override
	public String getName()
	{
		return "RANDOMPLAYER";
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.PLAYER;
	}

	@Override
	public Class<? extends Entity> getEntityClass()
	{
		return Player.class;
	}

	@Override
	public Set<Class<?>> getEntityClasses()
	{
		return null;
	}

	@Override
	public EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.NAMED;
	}

	@Override
	public Entity spawn(final Location location)
	{
		return NPCHelper.spawnPlayer(location, NamesHelper.getRandomName());
	}

	@Override
	public boolean matches(final Entity entity)
	{
		return entity instanceof Player && entity.hasMetadata("NPC");
	}
}
