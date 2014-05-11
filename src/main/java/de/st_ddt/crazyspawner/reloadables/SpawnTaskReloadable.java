package de.st_ddt.crazyspawner.reloadables;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.reloadable.Reloadable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public final class SpawnTaskReloadable implements Reloadable
{

	private final CrazySpawner plugin;

	/**
	 * @param plugin
	 */
	public SpawnTaskReloadable(final CrazySpawner plugin)
	{
		this.plugin = plugin;
	}

	@Override
	@Localized("CRAZYSPAWNER.SPAWNTASKS.RELOADED")
	public void reload(final CommandSender sender)
	{
		plugin.loadSpawnTasks();
		plugin.saveSpawnTasks();
		plugin.sendLocaleMessage("SPAWNABLEENTITIES.RELOADED", sender);
	}

	@Override
	@Permission("crazyspawner.reload.spawntasks")
	public boolean hasReloadPermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.reload.spawntasks") || sender.hasPermission("crazyspawner.reload.*");
	}

	@Override
	@Localized("CRAZYSPAWNER.SPAWNTASKS.SAVED")
	public void save(final CommandSender sender)
	{
		plugin.saveSpawnTasks();
		plugin.sendLocaleMessage("SPAWNABLEENTITIES.SAVED", sender);
	}

	@Override
	@Permission("crazyspawner.save.spawntasks")
	public boolean hasSavePermission(final CommandSender sender)
	{
		return sender.hasPermission("crazyspawner.save.spawntasks") || sender.hasPermission("crazyspawner.save.*");
	}
}
