package de.st_ddt.crazyspawner.reloadables;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.reloadable.Reloadable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public final class CustomEntitiesReloadable implements Reloadable
{

	private final CrazySpawner plugin;

	/**
	 * @param plugin
	 */
	public CustomEntitiesReloadable(final CrazySpawner plugin)
	{
		this.plugin = plugin;
	}

	@Override
	@Localized("CRAZYSPAWNER.SPAWNABLEENTITIES.RELOADED")
	public void reload(final CommandSender sender)
	{
		plugin.loadCustomEntities();
		plugin.saveCustomEntities();
		plugin.sendLocaleMessage("SPAWNABLEENTITIES.RELOADED", sender);
	}

	@Override
	@Permission("crazyspawner.reload.entities")
	public boolean hasReloadPermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.reload.entities") || sender.hasPermission("crazyspawner.reload.*");
	}

	@Override
	@Localized("CRAZYSPAWNER.SPAWNABLEENTITIES.SAVED")
	public void save(final CommandSender sender)
	{
		plugin.saveCustomEntities();
		plugin.sendLocaleMessage("SPAWNABLEENTITIES.SAVED", sender);
	}

	@Override
	@Permission("crazyspawner.save.entities")
	public boolean hasSavePermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.save.entities") || sender.hasPermission("crazyspawner.save.*");
	}
}
