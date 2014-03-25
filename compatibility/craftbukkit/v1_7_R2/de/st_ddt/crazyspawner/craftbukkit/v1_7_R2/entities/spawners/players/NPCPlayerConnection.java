package de.st_ddt.crazyspawner.craftbukkit.v1_7_R2.entities.spawners.players;

import net.minecraft.server.v1_7_R2.EntityPlayer;
import net.minecraft.server.v1_7_R2.MinecraftServer;
import net.minecraft.server.v1_7_R2.Packet;
import net.minecraft.server.v1_7_R2.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;

final class NPCPlayerConnection extends PlayerConnection
{

	public NPCPlayerConnection(final MinecraftServer minecraftServer, final EntityPlayer entityplayer)
	{
		super(minecraftServer, PlayerSpawnerImpl.getNetworkManager(), entityplayer);
	}

	@Override
	public CraftPlayer getPlayer()
	{
		return new CraftPlayer((CraftServer) Bukkit.getServer(), player);
	}

	@Override
	public void sendPacket(final Packet packet)
	{
	}
}
