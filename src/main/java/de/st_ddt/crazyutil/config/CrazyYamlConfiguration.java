package de.st_ddt.crazyutil.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class CrazyYamlConfiguration extends YamlConfiguration
{
	
	/**
	 * Used to format Dates to a readable String <b>yyyyMMddHHmmss</b>
	 */
public static DateFormat SHORTDATETIMEFORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public void load(final File file) throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		try
		{
			super.load(file);
		}
		catch (final IllegalArgumentException e)
		{
			throw e;
		}
		catch (final FileNotFoundException e)
		{
			throw e;
		}
		catch (final IOException e)
		{
			throw e;
		}
		catch (final InvalidConfigurationException e)
		{
			createBackup(file, e);
			throw e;
		}
	}

	public static File createBackup(final File file)
	{
		return createBackup(file, null);
	}

	private static File createBackup(final File file, final Throwable exception)
	{
		final String orgName = file.getName();
		final File parentFile = file.getParentFile();
		final String time = SHORTDATETIMEFORMAT.format(new Date());
		// Backup File
		final String bakName = orgName.replace(".yml", "_" + time + ".yml.bak");
		final File bakFile;
		if (bakName.equals(orgName))
			bakFile = new File(parentFile, orgName + "_" + time + ".yml.bak");
		else
			bakFile = new File(parentFile, bakName);
		try
		{
			file.renameTo(bakFile);
		}
		catch (final Throwable t)
		{
			System.err.println("Could not create backup-file " + bakFile.getName() + " for " + file.getPath());
			System.err.println("Caused by " + t.getClass().getName() + ": " + t.getMessage());
		}
		// Error File
		if (exception == null)
			return bakFile;
		final String errName = orgName.replace(".yml", "_" + time + ".yml.err");
		final File errFile;
		if (errName.equals(orgName))
			errFile = new File(parentFile, orgName + "_" + time + ".yml.err");
		else
			errFile = new File(parentFile, errName);
		try (PrintWriter writer = new PrintWriter(errFile))
		{
			exception.printStackTrace(writer);
		}
		catch (final Throwable t)
		{
			System.err.println("Could not create error-file " + errFile.getName() + " for " + file.getPath());
			System.err.println("Caused by " + t.getClass().getName() + ": " + t.getMessage());
		}
		return bakFile;
	}

	@Override
	public void save(final File file) throws IOException
	{
		file.getParentFile().mkdirs();
		super.save(file);
	}
}
