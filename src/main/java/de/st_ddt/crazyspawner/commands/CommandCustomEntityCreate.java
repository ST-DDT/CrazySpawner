package de.st_ddt.crazyspawner.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.spawners.CustomizedParentedSpawner;
import de.st_ddt.crazyspawner.entities.spawners.NamedParentedSpawner;
import de.st_ddt.crazyutil.entities.EntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawnerHelper;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandCustomEntityCreate extends CommandExecutor
{

	public CommandCustomEntityCreate(final CrazySpawner plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSPAWNER.COMMAND.CUSTOMENTITY.CREATED {Spawner} {ParentSpawner}")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 2)
			throw new CrazyCommandUsageException("<ParentSpawner/EntityType> <newName>");
		final String parentName = args[0];
		final String spawnerName = args[1];
		final EntitySpawner parentSpawner = NamedEntitySpawnerHelper.getNamedEntitySpawner(parentName);
		if (parentSpawner == null)
			throw new CrazyCommandNoSuchException("ParentSpawner/EntityType", parentName, NamedEntitySpawnerParamitrisable.tabHelp(parentName));
		final CustomizedParentedSpawner spawner = new CustomizedParentedSpawner(parentSpawner);
		final NamedParentedSpawner namedSpawner = new NamedParentedSpawner(spawner, spawnerName);
		owner.sendLocaleMessage("COMMAND.CUSTOMENTITY.CREATED", sender, namedSpawner.getName(), parentSpawner instanceof NamedEntitySpawner ? ((NamedEntitySpawner) parentSpawner).getName() : parentName);
		owner.addCustomEntity(namedSpawner);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		else
			return NamedEntitySpawnerParamitrisable.tabHelp(args[0]);
	}

	@Override
	@Permission("crazyspawner.customentity.create")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.customentity.create");
	}
}
