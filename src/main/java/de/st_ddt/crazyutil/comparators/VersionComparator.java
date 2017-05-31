package de.st_ddt.crazyutil.comparators;

import java.util.Comparator;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

/**
 * Helper Class to compare versions.
 */
public class VersionComparator implements Comparator<String>
{

	private VersionComparator()
	{
	}

	/**
	 * Compares to version Strings and returns 1, 0, -1 based on comparision.
	 * 
	 * @param version1
	 *            The first version to be compared with.
	 * @param version2
	 *            The second version to be compared with.
	 * @return -1 if version1 < version2<br>
	 *         0 if version1 = version2 <br>
	 *         1 if version1 > version2
	 */
	@Override
	public int compare(final String version1, final String version2)
	{
		return compareVersions(version1, version2);
	}

	/**
	 * Compares to version Strings and returns 1, 0, -1 based on comparision.
	 * 
	 * @param version1
	 *            The first version to be compared with.
	 * @param version2
	 *            The second version to be compared with.
	 * @return -1 if version1 < version2<br>
	 *         0 if version1 = version2 <br>
	 *         1 if version1 > version2
	 */
	public static int compareVersions(final String version1, final String version2)
	{
		final String[] split1 = version1.split("\\.");
		final String[] split2 = version2.split("\\.");
		final int length1 = split1.length;
		final int length2 = split2.length;
		final int length = Math.max(length1, length2);
		final Integer[] v1 = new Integer[length];
		final Integer[] v2 = new Integer[length];
		for (int i = 0; i < length1; i++)
			v1[i] = cleanVersion(split1[i]);
		for (int i = length1; i < length; i++)
			v1[i] = 0;
		for (int i = 0; i < length2; i++)
			v2[i] = cleanVersion(split2[i]);
		for (int i = length2; i < length; i++)
			v2[i] = 0;
		int i = 0;
		int res = 0;
		while (res == 0 && i < length)
		{
			res = v1[i].compareTo(v2[i]);
			i++;
		}
		return res;
	}

	/**
	 * Compares to version Strings and returns 1, 0, -1 based on comparision.
	 * 
	 * @param version1
	 *            The plugin to be compared with.
	 * @param version2
	 *            The version to be compared with.
	 * @return -1 if version1 < version2<br>
	 *         0 if version1 = version2 <br>
	 *         1 if version1 > version2
	 */
	public static int compareVersions(final CrazyLightPluginInterface plugin, final String version)
	{
		return compareVersions(plugin.getVersion(), version);
	}

	/**
	 * Converts a String to a Integer but drops every character after a none numeric one.
	 * 
	 * @param version
	 *            The partial version string to be converted.
	 * @return The version number represented by <b>version</b>
	 */
	private static int cleanVersion(final String version)
	{
		try
		{
			return Integer.valueOf(version);
		}
		catch (final NumberFormatException e)
		{
			int res = 0;
			for (final char cha : version.toCharArray())
			{
				switch (cha)
				{
					case '0':
						res += 0;
						break;
					case '1':
						res += 1;
						break;
					case '2':
						res += 2;
						break;
					case '3':
						res += 3;
						break;
					case '4':
						res += 4;
						break;
					case '5':
						res += 5;
						break;
					case '6':
						res += 6;
						break;
					case '7':
						res += 7;
						break;
					case '8':
						res += 8;
						break;
					case '9':
						res += 9;
						break;
					default:
						return res;
				}
				res *= 10;
			}
			return res;
		}
	}
}
