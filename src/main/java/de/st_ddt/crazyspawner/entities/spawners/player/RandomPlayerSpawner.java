package de.st_ddt.crazyspawner.entities.spawners.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.NamesHelper;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;

public final class RandomPlayerSpawner extends BasicPlayerSpawner
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
	public final EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.NAMED;
	}

	@Override
	public Player spawn(final Location location)
	{
		return spawn(location, NamesHelper.getRandomName());
	}

	@Override
	public boolean matches(final Player player)
	{
		return true;
	}
}
