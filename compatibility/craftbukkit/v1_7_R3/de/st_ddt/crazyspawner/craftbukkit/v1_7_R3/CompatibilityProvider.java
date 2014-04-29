package de.st_ddt.crazyspawner.craftbukkit.v1_7_R3;

import de.st_ddt.crazyspawner.craftbukkit.v1_7_R3.entities.spawners.players.PlayerSpawnerImpl;
import de.st_ddt.crazyspawner.craftbukkit.v1_7_R3.entities.util.AttributeHelperImpl;
import de.st_ddt.crazyspawner.entities.spawners.player.PlayerSpawnerInterface;
import de.st_ddt.crazyspawner.entities.util.AttributeHelperInterface;

public class CompatibilityProvider
{

	static
	{
		PlayerSpawnerInterface.PLAYERSPAWNERCLASSES.add(PlayerSpawnerImpl.class);
		AttributeHelperInterface.ATTRIBUTEHELPERCLASSES.add(AttributeHelperImpl.class);
	}

	private CompatibilityProvider()
	{
	}
}
