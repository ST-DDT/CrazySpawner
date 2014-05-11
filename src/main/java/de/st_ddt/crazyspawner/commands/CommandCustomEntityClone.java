package de.st_ddt.crazyspawner.commands;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.spawners.NamedParentedSpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandCustomEntityClone extends CommandExecutor
{

	private final static Pattern PATTERN_NAME = Pattern.compile("\\:");

	public CommandCustomEntityClone(final CrazySpawner plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSPAWNER.COMMAND.CUSTOMENTITY.CLONED {Spawner} {ClonedSpawner}")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 2)
			throw new CrazyCommandUsageException("<CustomSpawner> <newName>");
		String clonedName = args[0];
		if (clonedName.contains(":"))
		{
			final String[] split = PATTERN_NAME.split(clonedName);
			clonedName = split[split.length - 1];
		}
		final String spawnerName = args[1];
		final NamedParentedSpawner parentSpawner = owner.getCustomEntities().get(clonedName);
		if (parentSpawner == null)
			throw new CrazyCommandNoSuchException("CustomSpawner", clonedName);
		final NamedParentedSpawner namedSpawner = parentSpawner.clone(spawnerName);
		owner.sendLocaleMessage("COMMAND.CUSTOMENTITY.CLONED", sender, namedSpawner.getName(), parentSpawner instanceof NamedEntitySpawner ? ((NamedEntitySpawner) parentSpawner).getName() : clonedName);
		owner.addCustomEntity(namedSpawner);
		owner.saveCustomEntities();
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		else
			return NamedEntitySpawnerParamitrisable.tabHelp(owner.getCustomEntities(), args[0]);
	}

	@Override
	@Permission("crazyspawner.customentity.create")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.customentity.create");
	}
}
