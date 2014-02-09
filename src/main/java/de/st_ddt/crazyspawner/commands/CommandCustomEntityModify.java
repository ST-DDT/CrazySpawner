package de.st_ddt.crazyspawner.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CustomizedParentedSpawner;
import de.st_ddt.crazyspawner.entities.NamedParentedSpawner;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawnerHelper;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandCustomEntityModify extends CommandExecutor
{

	public CommandCustomEntityModify(final CrazySpawner plugin)
	{
		super(plugin);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Localized({ "CRAZYSPAWNER.COMMAND.CUSTOMENTITY.MODIFY.UNSUPPORTED {Name} {EntityType} {SpawnerType}", "CRAZYSPAWNER.COMMAND.CUSTOMENTITY.MODIFY {Name} {EntityType}" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length < 2)
			throw new CrazyCommandUsageException("<name:String> [Params...]");
		final String edited = args[0];
		final NamedParentedSpawner namedSpawner = owner.getCustomEntities().get(edited);
		if (namedSpawner == null)
			throw new CrazyCommandNoSuchException("NamedSpawner", edited);
		final EntityType type = namedSpawner.getEntityType();
		if (!(namedSpawner.getParentSpawner() instanceof CustomizedParentedSpawner))
		{
			owner.sendLocaleMessage("COMMAND.CUSTOMENTITY.MODIFY.UNSUPPORTED", sender, edited, EntitySpawnerHelper.getNiceEntityTypeName(type), namedSpawner.getSpawnerType().name());
			return;
		}
		
		
		
		
		
		
		
		final EntityType type;
		final StringParamitrisable nameParam;
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final NamedEntitySpawner spawner = NamedEntitySpawnerHelper.getNamedEntitySpawner(inheritance);
		if (spawner == null || !(spawner instanceof LegacyEntitySpawner))
		{
			type = EntityType.valueOf(inheritance.toUpperCase());
			if (type == null)
				throw new CrazyCommandNoSuchException("Inheritance/EntityType", inheritance, EnumParamitrisable.getEnumNames(EntitySpawnerHelper.getSpawnableEntityTypes()));
			nameParam = EntityPropertyHelper.getCommandParams(type, params, sender);
		}
		else
		{
			type = spawner.getType();
			nameParam = ((LegacyEntitySpawner) spawner).getCommandParams(params, sender);
		}
		ChatHelperExtended.readParameters(ChatHelperExtended.shiftArray(args, 1), new HashMap<String, TabbedParamitrisable>(params), nameParam);
		if (nameParam.getValue() == null)
			throw new CrazyCommandUsageException("<Inheritance/EntityType> <name:String> [Params...]");
		final LegacyEntitySpawner entitySpawner = new LegacyEntitySpawner(type, params);
		owner.addCustomEntity(entitySpawner);
		owner.sendLocaleMessage("COMMAND.MODIFYENTITY", sender, type.getName() == null ? type.name() : type.getName(), nameParam.getValue());
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length == 1)
		{
			final List<String> res = new ArrayList<String>();
			res.addAll(EnumParamitrisable.getEnumNames(EntitySpawnerHelper.getSpawnableEntityTypes()));
			for (final LegacyEntitySpawner spawner : owner.getCustomEntities().values())
				res.add(spawner.getName().toUpperCase());
			final String inheritance = args[0].toUpperCase();
			final Iterator<String> it = res.iterator();
			while (it.hasNext())
				if (!it.next().startsWith(inheritance))
					it.remove();
			return res;
		}
		else
		{
			final String inheritance = args[0];
			final EntityType type;
			final StringParamitrisable nameParam;
			final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
			final NamedEntitySpawner spawner = NamedEntitySpawnerHelper.getNamedEntitySpawner(inheritance);
			if (spawner == null || !(spawner instanceof LegacyEntitySpawner))
			{
				type = EntityType.valueOf(inheritance.toUpperCase());
				if (type == null)
					return new ArrayList<String>(0);
				nameParam = EntityPropertyHelper.getCommandParams(type, params, sender);
			}
			else
			{
				type = spawner.getType();
				nameParam = ((LegacyEntitySpawner) spawner).getCommandParams(params, sender);
			}
			return ChatHelperExtended.tabHelp(ChatHelperExtended.shiftArray(args, 1), params, nameParam);
		}
	}

	@Override
	@Permission("crazyspawner.modifyentity")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.modifyentity");
	}
}
