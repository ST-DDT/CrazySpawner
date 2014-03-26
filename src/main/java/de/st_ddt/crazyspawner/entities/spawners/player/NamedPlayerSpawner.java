package de.st_ddt.crazyspawner.entities.spawners.player;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.entities.ConfigurableEntitySpawner;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public final class NamedPlayerSpawner extends BasicPlayerSpawner implements ConfigurableEntitySpawner
{

	protected final String name;

	public NamedPlayerSpawner()
	{
		super();
		this.name = "Player";
	}

	public NamedPlayerSpawner(final String name)
	{
		super();
		this.name = name;
	}

	public NamedPlayerSpawner(final ConfigurationSection config)
	{
		super();
		this.name = null;
		// TODO Auto-generated method stub
	}

	public NamedPlayerSpawner(final Map<String, ? extends Paramitrisable> params)
	{
		super();
		this.name = null;
		// TODO Auto-generated method stub
	}

	@Override
	public String getName()
	{
		return "PLAYER";
	}

	@Override
	public Player spawn(final Location location)
	{
		return spawn(location, name);
	}

	@Override
	public boolean matches(final Player player)
	{
		return player.getName().equals(name);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		// TODO Auto-generated method stub
	}
}
