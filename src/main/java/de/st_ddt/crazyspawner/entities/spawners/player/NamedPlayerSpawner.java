package de.st_ddt.crazyspawner.entities.spawners.player;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.entities.ConfigurableEntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;
import de.st_ddt.crazyutil.paramitrisable.ColoredStringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public final class NamedPlayerSpawner extends BasicPlayerSpawner implements ConfigurableEntitySpawner
{

	protected final String playerName;

	public NamedPlayerSpawner()
	{
		super();
		this.playerName = "Player";
	}

	public NamedPlayerSpawner(final String name)
	{
		super();
		this.playerName = name;
	}

	public NamedPlayerSpawner(final ConfigurationSection config)
	{
		super();
		this.playerName = ChatHelper.colorise(config.getString("playerName", "Player"));
	}

	public NamedPlayerSpawner(final Map<String, ? extends Paramitrisable> params)
	{
		super();
		final StringParamitrisable playerNameParam = (StringParamitrisable) params.get("playername");
		this.playerName = playerNameParam.getValue();
	}

	@Override
	public String getName()
	{
		return "PLAYER";
	}

	@Override
	public final EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.SPECIAL;
	}

	@Override
	public Player spawn(final Location location)
	{
		return spawn(location, playerName);
	}

	@Override
	public boolean matches(final Player player)
	{
		return player.getName().equals(playerName);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final StringParamitrisable playerNameParam = new ColoredStringParamitrisable(playerName);
		params.put("pname", playerNameParam);
		params.put("plrname", playerNameParam);
		params.put("playername", playerNameParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "playerName", ChatHelper.decolorise(playerName));
	}
}
