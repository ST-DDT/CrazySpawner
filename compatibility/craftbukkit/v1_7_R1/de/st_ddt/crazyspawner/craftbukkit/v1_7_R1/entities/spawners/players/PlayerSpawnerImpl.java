package de.st_ddt.crazyspawner.craftbukkit.v1_7_R1.entities.spawners.players;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.MinecraftServer;
import net.minecraft.server.v1_7_R1.PlayerInteractManager;
import net.minecraft.server.v1_7_R1.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import de.st_ddt.crazyspawner.entities.spawners.player.PlayerSpawnerInterface;

public class PlayerSpawnerImpl implements PlayerSpawnerInterface
{

	private final static Field worldServer;
	private final static Field minecraftServer;
	private final static NPCNetworkManager networkManager = new NPCNetworkManager();
	private static int number;
	static
	{
		Field _worldServer = null;
		Field _minecraftServer = null;
		try
		{
			_worldServer = CraftWorld.class.getDeclaredField("world");
			_worldServer.setAccessible(true);
			_minecraftServer = CraftServer.class.getDeclaredField("console");
			_minecraftServer.setAccessible(true);
		}
		catch (final Throwable t)
		{}
		worldServer = _worldServer;
		minecraftServer = _minecraftServer;
	}

	static WorldServer getMinecraftWorld(final World world) throws Exception
	{
		return (WorldServer) worldServer.get(world);
	}

	static MinecraftServer getMinecraftServer() throws Exception
	{
		final Server server = Bukkit.getServer();
		return (MinecraftServer) minecraftServer.get(server);
	}

	static NPCNetworkManager getNetworkManager()
	{
		return networkManager;
	}

	@Override
	public Class<CraftPlayer> getPlayerClass()
	{
		return CraftPlayer.class;
	}

	@Override
	public Player spawnPlayer(final Location location, final String name)
	{
		try
		{
			final String realName;
			if (name.length() > 16)
				realName = name.substring(0, 16);
			else
				realName = name;
			final World world = location.getWorld();
			final WorldServer mcWorld = getMinecraftWorld(world);
			final GameProfile gameprofile = new GameProfile("NPC_" + name + "_" + (number++), realName);
			final PlayerInteractManager playerinteractmanager = new PlayerInteractManager(mcWorld);
			final EntityPlayer entity = new NPCPlayer(getMinecraftServer(), mcWorld, gameprofile, playerinteractmanager);
			final Player player = entity.getBukkitEntity();
			entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			mcWorld.addEntity(entity, SpawnReason.CUSTOM);
			return player;
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean hasCreated(final Player player)
	{
		if (player instanceof CraftPlayer)
		{
			final CraftPlayer craftPlayer = (CraftPlayer) player;
			return craftPlayer.getHandle() instanceof NPCPlayer;
		}
		else
			return false;
	}
}
