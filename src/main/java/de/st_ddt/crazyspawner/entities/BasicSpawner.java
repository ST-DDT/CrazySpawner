package de.st_ddt.crazyspawner.entities;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.entities.EntitySpawner;

public abstract class BasicSpawner implements EntitySpawner, ConfigurationSaveable
{

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "spawnerType", getSpawnerType().name());
	}
}
