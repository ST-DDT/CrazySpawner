package de.st_ddt.crazyutil.entities;

import org.bukkit.command.CommandSender;

public interface ShowableEntitySpawner extends EntitySpawner
{

	/**
	 * Shows information about this spawner to the given target.
	 * 
	 * @param target
	 *            The target the information should be displayed too.
	 */
	public void show(CommandSender target);
}
