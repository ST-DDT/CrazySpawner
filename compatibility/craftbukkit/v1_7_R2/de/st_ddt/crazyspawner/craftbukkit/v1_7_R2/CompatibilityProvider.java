package de.st_ddt.crazyspawner.craftbukkit.v1_7_R2;

import de.st_ddt.crazyspawner.craftbukkit.v1_7_R2.entities.spawners.players.PlayerSpawnerImpl;
import de.st_ddt.crazyspawner.entities.spawners.player.PlayerSpawnerInterface;

public class CompatibilityProvider
{

	static
	{
		PlayerSpawnerInterface.PLAYERSPAWNERCLASSES.add(PlayerSpawnerImpl.class);
	}

	private CompatibilityProvider()
	{
	}
}
