package de.st_ddt.crazyspawner.entities.properties;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.tests.DummyServer;

public class EntityPropertyHelperTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Bukkit.setServer(new DummyServer());
	}

	@Test
	public void testgetDefaultEntityProperties()
	{
		for (final EntityType type : EntityType.values())
			EntityPropertyHelper.getDefaultEntityProperties(type);
	}

	@Test
	public void testgetEntityPropertiesFromConfig()
	{
		for (final EntityType type : EntityType.values())
			EntityPropertyHelper.getEntityPropertiesFromConfig(type, new YamlConfiguration());
	}

	@Test
	public void testCommandParams()
	{
		for (final EntityType type : EntityType.values())
		{
			final Map<String, TabbedParamitrisable> params = new HashMap<String, TabbedParamitrisable>();
			EntityPropertyHelper.getCommandParams(type, params, null);
			EntityPropertyHelper.getEntityPropertiesFromParams(type, params);
		}
	}

	@Test
	public void testCommandParamsCount()
	{
		Assert.assertTrue("There should be at least one EntityProperty registered!", EntityPropertyHelper.getTotalPropertiesCount() > 0);
		Assert.assertTrue("There should be at least one command parameter attached to the registered EntityProperties!", EntityPropertyHelper.getTotalCommandParamsCount() > 0);
	}
}
