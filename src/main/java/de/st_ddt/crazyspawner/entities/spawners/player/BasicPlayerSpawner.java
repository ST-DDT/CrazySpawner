package de.st_ddt.crazyspawner.entities.spawners.player;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;


import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.spawners.BasicSpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;

public abstract class BasicPlayerSpawner extends BasicSpawner implements NamedEntitySpawner
{

	private static PlayerSpawnerInterface playerSpawner;

	public static boolean initialize()
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
	public final EntityType getEntityType()
	{
		return EntityType.PLAYER;
	}

	@Override
	public final Set<Class<?>> getEntityClasses()
	{
		return null; // TODO: Check this
	}

	@Override
	public abstract Player spawn(final Location location);

	public final Player spawn(final Location location, final String name)
	{
		return spawnPlayer(location, name);
	}

	public static Player spawnPlayer(final Location location, final String name)
	{
		if (playerSpawner == null)
			return location.getWorld().spawn(location, Player.class);
		else
		{
			final Player player = playerSpawner.spawnPlayer(location, name);
			if (player != null)
				player.setMetadata("NPC", new FixedMetadataValue(CrazySpawner.getPlugin(), "CrazySpawner"));
			return player;
		}
	}

	@Override
	public final boolean matches(final Entity entity)
	{
		if (!(entity instanceof Player))
			return false;
		else
		{
			final Player player = (Player) entity;
			return matches(player) && matchesSpawnedPlayer(player);
		}
	}

	public static boolean matchesSpawnedPlayer(final Player player)
	{
		if (playerSpawner == null)
			return player.hasMetadata("NPC");
		else
			return player.hasMetadata("NPC") && playerSpawner.hasCreated(player);
	}

	public abstract boolean matches(final Player player);
}
