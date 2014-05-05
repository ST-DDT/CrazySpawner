package de.st_ddt.crazyspawner.commands;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.paramitrisable.CreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandCreatureSpawner extends CommandExecutor
{

	private final Map<Player, EntityType> creatureSelection;

	public CommandCreatureSpawner(final CrazySpawner plugin, final Map<Player, EntityType> creatureSelection)
	{
		super(plugin);
		this.creatureSelection = creatureSelection;
	}

	@Override
	@Permission({ "crazyspawner.creaturespawner.*", "crazyspawner.creaturespawner.<ENTITYTYPE>" })
	@Localized("CRAZYSPAWNER.COMMAND.CREATURESPAWNER.SELECTED {Creature}")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		final Map<String, Paramitrisable> params = new TreeMap<String, Paramitrisable>();
		final CreatureParamitrisable typeParam = new CreatureParamitrisable(null);
		params.put("c", typeParam);
		params.put("creature", typeParam);
		ChatHelperExtended.readParameters(args, params, typeParam);
		final EntityType type = typeParam.getValue();
		if (type == null)
			throw new CrazyCommandUsageException("<Creature>");
		if (!(player.hasPermission("crazyspawner.creaturespawner.*") || player.hasPermission("crazyspawner.creaturespawner." + type.name())))
			throw new CrazyCommandPermissionException();
		creatureSelection.put(player, type);
		owner.sendLocaleMessage("COMMAND.CREATURESPAWNER.SELECTED", player, type);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new TreeMap<String, Tabbed>();
		final CreatureParamitrisable creature = new CreatureParamitrisable(null);
		params.put("c", creature);
		params.put("creature", creature);
		return ChatHelperExtended.tabHelp(args, params, creature);
	}

	@Override
	@Permission("crazyspawner.creaturespawner")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.creaturespawner");
	}

	@Override
	public boolean isAccessible(final CommandSender sender)
	{
		if (sender instanceof Player)
			return super.isAccessible(sender);
		else
			return false;
	}
}
