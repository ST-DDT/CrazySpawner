package de.st_ddt.crazyspawner.entities.spawners.player;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.entities.EntitySpawnerType;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;

abstract class BasicPlayerSpawner implements NamedEntitySpawner
{

	private static PlayerSpawnerInterface playerSpawner;

	static boolean initialize()
	{
		for (final Class<? extends PlayerSpawnerInterface> clazz : PlayerSpawnerInterface.PLAYERSPAWNERCLASSES)
			try
			{
				playerSpawner = clazz.newInstance();
				return true;
			}
			catch (final Throwable t)
			{}
		return false;
	}

	public BasicPlayerSpawner()
	{
		super();
	}

	@Override
	public String getName()
	{
		return "PLAYER";
	}

	@Override
	public final EntityType getEntityType()
	{
		return EntityType.PLAYER;
	}

	@Override
	public final Class<Player> getEntityClass()
	{
		return Player.class;
	}

	@Override
	public final Set<Class<?>> getEntityClasses()
	{
		return null; // TODO: Check this
	}

	@Override
	public final EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.SPECIAL;
	}

	@Override
	public abstract Player spawn(final Location location);

	public final Player spawn(final Location location, final String name)
	{
		if (playerSpawner == null)
			return location.getWorld().spawn(location, Player.class);
		else
			return playerSpawner.spawnPlayer(location, name);
	}

	@Override
	public final boolean matches(final Entity entity)
	{
		if (!(entity instanceof Player))
			return false;
		else
		{
			final Player player = (Player) entity;
			if (playerSpawner == null)
				return matches(player) && entity.hasMetadata("NPC");
			else
				return matches(player) && entity.hasMetadata("NPC") && playerSpawner.hasCreated(player);
		}
	}

	public abstract boolean matches(final Player player);
}
