package de.st_ddt.crazyutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NamesHelper
{

	private final static Charset UTF8 = Charset.forName("UTF-8");
	private final static Random RANDOM = new Random();
	private final static ArrayList<String> NAMES = new ArrayList<String>();

	public static void loadNames(final File file)
	{
		if (file == null)
			return;
		try (final FileInputStream stream = new FileInputStream(file);
				final InputStreamReader reader = new InputStreamReader(stream, UTF8);
				final BufferedReader buffered = new BufferedReader(reader);)
		{
			String line = null;
			while ((line = buffered.readLine()) != null)
				if (line.length() > 0)
					NAMES.add(line);
		}
		catch (final Exception e)
		{
			System.err.println("[CrazySpawner] Could not read Names file: " + file.getName());
			System.err.println(e.getMessage());
		}
	}

	public static String getRandomName()
	{
		if (NAMES.isEmpty())
			return null;
		else
			return NAMES.get(RANDOM.nextInt(NAMES.size()));
	}

	public static List<String> getAllNames()
	{
		return NAMES;
	}

	private NamesHelper()
	{
	}
}
