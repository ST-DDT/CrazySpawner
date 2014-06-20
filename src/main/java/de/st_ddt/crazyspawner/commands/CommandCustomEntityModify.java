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
import de.st_ddt.crazyspawner.entities.spawners.CustomizedParentedSpawner;
import de.st_ddt.crazyspawner.entities.spawners.NamedParentedSpawner;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.entities.ChangeableConfigurableEntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandCustomEntityModify extends CommandExecutor
{

	public CommandCustomEntityModify(final CrazySpawner plugin)
	{
		super(plugin);
	}

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
		final ChangeableConfigurableEntitySpawner changeableSpawner = (ChangeableConfigurableEntitySpawner) namedSpawner.getParentSpawner();
		final Map<String, TabbedParamitrisable> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		changeableSpawner.getCommandParams(params, sender);
		final Map<String, TabbedParamitrisable> paramsRead = new HashMap<>(params);
		ChatHelperExtended.readParameters(ChatHelperExtended.shiftArray(args, 1), params);
		final NamedParentedSpawner entitySpawner = new NamedParentedSpawner(changeableSpawner.change(paramsRead), namedSpawner.getName());
		owner.sendLocaleMessage("COMMAND.CUSTOMENTITY.MODIFY", sender, namedSpawner.getName(), EntitySpawnerHelper.getNiceEntityTypeName(type));
		owner.addCustomEntity(entitySpawner);
		owner.saveCustomEntities();
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length == 1)
		{
			final List<String> res = new ArrayList<String>();
			res.addAll(EnumParamitrisable.getEnumNames(EntitySpawnerHelper.getSpawnableEntityTypes()));
			for (final NamedParentedSpawner spawner : owner.getCustomEntities().values())
				if (spawner.getParentSpawner() instanceof ChangeableConfigurableEntitySpawner)
					res.add(spawner.getName().toUpperCase());
			final String name = args[0].toUpperCase();
			final Iterator<String> it = res.iterator();
			while (it.hasNext())
				if (!it.next().startsWith(name))
					it.remove();
			return res;
		}
		else
		{
			final String edited = args[0];
			final NamedParentedSpawner namedSpawner = owner.getCustomEntities().get(edited);
			if (namedSpawner == null)
				return new ArrayList<>();
			if (!(namedSpawner.getParentSpawner() instanceof CustomizedParentedSpawner))
				return new ArrayList<>();
			final ChangeableConfigurableEntitySpawner changeableSpawner = (ChangeableConfigurableEntitySpawner) namedSpawner.getParentSpawner();
			final Map<String, TabbedParamitrisable> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			changeableSpawner.getCommandParams(params, sender);
			return ChatHelperExtended.tabHelp(ChatHelperExtended.shiftArray(args, 1), params);
		}
	}

	@Override
	@Permission("crazyspawner.modifyentity")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.modifyentity");
	}
}
