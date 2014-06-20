package de.st_ddt.crazyspawner.entities.spawners.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PlayerSpawnerInterface
{

	public List<Class<? extends PlayerSpawnerInterface>> PLAYERSPAWNERCLASSES = new ArrayList<>();

	public Class<? extends Player> getPlayerClass();

	public Player spawnPlayer(Location location, String name);

	public boolean hasCreated(Player player);
}
