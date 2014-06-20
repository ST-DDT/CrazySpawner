package de.st_ddt.crazyspawner.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.entities.ApplyableEntitySpawner;
import de.st_ddt.crazyutil.entities.ApplyableEntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawnerHelper;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandOverwriteEntity extends CommandExecutor
{

	public CommandOverwriteEntity(final CrazySpawner plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSPAWNER.COMMAND.OVERWRITEENTITY {EntityType} {CustomEntity}")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 2)
			throw new CrazyCommandUsageException("<EntityType> <CustomEntity>");
		final EntityType type = EntityType.valueOf(args[0].toUpperCase());
		if (type == null)
			throw new CrazyCommandNoSuchException("EntityType", args[0], tab(sender, args));
		final ApplyableEntitySpawner spawner;
		if (args[1].equalsIgnoreCase("DEFAULT"))
			spawner = null;
		else
		{
			spawner = ApplyableEntitySpawnerHelper.wrapSpawner(NamedEntitySpawnerHelper.getNamedEntitySpawner(args[1]));
			if (spawner == null || spawner.getEntityType() != type)
				throw new CrazyCommandNoSuchException("CustomEntity", args[1], tab(sender, args));
		}
		owner.getOverwriteEntities()[type.ordinal()] = spawner;
		owner.sendLocaleMessage("COMMAND.OVERWRITEENTITY", sender, type.name(), args[1].toUpperCase());
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length == 1)
		{
			final String arg = args[0].toUpperCase();
			final List<String> res = new ArrayList<String>();
			for (final String type : EnumParamitrisable.getEnumNames(EntitySpawnerHelper.getSpawnableEntityTypes()))
				if (type.startsWith(arg))
					res.add(type);
			return res;
		}
		else if (args.length == 2)
		{
			final EntityType type = EntityType.valueOf(args[0].toUpperCase());
			if (type == null)
				return null;
			final String arg = args[1].toUpperCase();
			final List<String> res = new ArrayList<String>();
			for (final NamedEntitySpawner spawner : NamedEntitySpawnerHelper.getNamedEntitySpawners(type))
				if (ApplyableEntitySpawnerHelper.isApplicable(spawner))
					if (spawner.getName().startsWith(arg))
						res.add(spawner.getName());
			if ("DEFAULT".startsWith(arg))
				res.add("DEFAULT");
			return res;
		}
		else
			return null;
	}

	@Override
	@Permission("crazyspawner.overwriteentity")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.overwriteentity");
	}
}
