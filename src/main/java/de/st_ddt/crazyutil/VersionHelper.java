package de.st_ddt.crazyutil;

import org.bukkit.Bukkit;

import de.st_ddt.crazyutil.comparators.VersionComparator;

public class VersionHelper
{

	private static String minecraftVersion = Bukkit.getBukkitVersion().split("-", 2)[0];
	private static String bukkitVersion = Bukkit.getBukkitVersion().replace("-R", ".").split("-", 2)[0];

	/**
	 * @return Minecraft's current version.
	 */
	public static String getMinecraftVersion()
	{
		return minecraftVersion;
	}

	/**
	 * @return Bukkit's current version.
	 */
	public static String getBukkitVersion()
	{
		return bukkitVersion;
	}

	/**
	 * Checks whether Bukkit's version is newer than the given version.
	 * 
	 * @param version
	 *            The version to compare to Bukkit's version.
	 * @return True, if the given version is either the same or older than Bukkit's version. False otherwise.
	 */
	public static boolean hasRequiredVersion(final String version)
	{
		return VersionComparator.compareVersions(bukkitVersion, version) >= 0;
	}
}