package de.st_ddt.crazyspawner.craftbukkit.v1_7_R2;

import de.st_ddt.crazyspawner.craftbukkit.v1_7_R2.entities.spawners.PlayerSpawner;
import de.st_ddt.crazyspawner.craftbukkit.v1_7_R2.entities.spawners.RandomPlayerSpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.NamedEntitySpawnerHelper;

public class CompatibilityProvider
{

	static
	{
		final PlayerSpawner playerSpawner = new PlayerSpawner("Player");
		EntitySpawnerHelper.registerEntitySpawner(playerSpawner);
		NamedEntitySpawnerHelper.registerNamedEntitySpawner(playerSpawner);
		NamedEntitySpawnerHelper.registerNamedEntitySpawner(new RandomPlayerSpawner());
	}

	private CompatibilityProvider()
	{
	}
}
