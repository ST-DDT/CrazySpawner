package de.st_ddt.crazyspawner.entities.spawners.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.NamesHelper;

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
