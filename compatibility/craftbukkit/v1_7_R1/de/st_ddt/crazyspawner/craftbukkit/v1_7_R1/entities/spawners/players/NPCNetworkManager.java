package de.st_ddt.crazyspawner.craftbukkit.v1_7_R1.entities.spawners.players;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R1.NetworkManager;
import net.minecraft.util.io.netty.channel.Channel;

public class NPCNetworkManager extends NetworkManager
{

	private final static Field channel;
	static
	{
		Field _channel = null;
		try
		{
			_channel = NetworkManager.class.getDeclaredField("k");
			_channel.setAccessible(true);
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
		}
		channel = _channel;
	}

	public NPCNetworkManager()
	{
		super(false);
	}

	public void close()
	{
		try
		{
			final Channel c = (Channel) channel.get(this);
			c.close();
		}
		catch (final Throwable t)
		{}
	}
}
