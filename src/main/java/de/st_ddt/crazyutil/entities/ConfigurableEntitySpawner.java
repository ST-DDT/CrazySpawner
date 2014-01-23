package de.st_ddt.crazyutil.entities;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public interface ConfigurableEntitySpawner extends EntitySpawner, ConfigurationSaveable
{

	// public ConfigurableEntitySpawner(ConfigurationSection config);
	// public ConfigurableEntitySpawner(EntityType type, Map<String, ? extends Paramitrisable> params);
	public void getCommandParams(Map<String, ? super TabbedParamitrisable> params, CommandSender sender);

	@Override
	public void save(ConfigurationSection config, String path);
}
