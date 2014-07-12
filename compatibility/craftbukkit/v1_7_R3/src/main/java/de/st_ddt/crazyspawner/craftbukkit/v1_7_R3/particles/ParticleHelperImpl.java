package de.st_ddt.crazyspawner.craftbukkit.v1_7_R3.particles;

import java.util.Collection;
import java.util.LinkedList;

import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.particles.MaterialParticleType;
import de.st_ddt.crazyutil.particles.Particle;
import de.st_ddt.crazyutil.particles.ParticleHelperInterface;

public class ParticleHelperImpl implements ParticleHelperInterface
{

	private final static double PLAYERRANGE = 20;

	@Override
	public void castParticle(final Particle particle, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount)
	{
		castParticle(particle, location, offsetX, offsetY, offsetZ, speed, amount, getPlayers(location));
	}

	@Override
	public void castParticle(final Particle particle, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Player... players)
	{
		final Packet packet = createParticlePacket(particle, location, offsetX, offsetY, offsetZ, speed, amount);
		for (final Player player : players)
			sendPacket(player, packet);
	}

	@Override
	public void castParticle(final Particle particle, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Collection<Player> players)
	{
		final Packet packet = createParticlePacket(particle, location, offsetX, offsetY, offsetZ, speed, amount);
		for (final Player player : players)
			sendPacket(player, packet);
	}

	@Override
	public void castMaterialParticle(final MaterialParticleType type, final Material material, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount)
	{
		castMaterialParticle(type, material, data, location, offsetX, offsetY, offsetZ, speed, amount, getPlayers(location));
	}

	@Override
	public void castMaterialParticle(final MaterialParticleType type, final Material material, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Player... players)
	{
		final Packet packet = createMaterialParticlePacket(type, material, data, location, offsetX, offsetY, offsetZ, speed, amount);
		for (final Player player : players)
			sendPacket(player, packet);
	}

	@Override
	public void castMaterialParticle(final MaterialParticleType type, final Material material, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Collection<Player> players)
	{
		final Packet packet = createMaterialParticlePacket(type, material, data, location, offsetX, offsetY, offsetZ, speed, amount);
		for (final Player player : players)
			sendPacket(player, packet);
	}

	private static void sendPacket(final Player player, final Packet packet)
	{
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	private static PacketPlayOutWorldParticles createParticlePacket(final Particle particle, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount)
	{
		return createParticlePacket(getParticleName(particle), location, offsetX, offsetY, offsetZ, speed, amount);
	}

	private static PacketPlayOutWorldParticles createMaterialParticlePacket(final MaterialParticleType type, final Material material, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount)
	{
		return createParticlePacket(getParticleName(type, material, data), location, offsetX, offsetY, offsetZ, speed, amount);
	}

	private static PacketPlayOutWorldParticles createParticlePacket(final String particleName, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount)
	{
		if (particleName == null)
			return null;
		else
			return new PacketPlayOutWorldParticles(particleName, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offsetX, offsetY, offsetZ, speed, amount);
	}

	private static Collection<Player> getPlayers(final Location location)
	{
		final Collection<Player> res = new LinkedList<Player>();
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getWorld() == location.getWorld())
				if (location.distance(player.getLocation()) <= PLAYERRANGE)
					res.add(player);
		return res;
	}

	private static String getParticleName(final Particle particle)
	{
		switch (particle)
		{
			case HUGE_EXPLOSION:
				return "hugeexplosion";
			case LARGE_EXPLODE:
				return "largeexplode";
			case FIREWORKS_SPARK:
				return "fireworksSpark";
			case BUBBLE:
				return "bubble";
			case SUSPEND:
				return "suspend";
			case DEPTH_SUSPEND:
				return "depthSuspend";
			case TOWN_AURA:
				return "townaura";
			case CRIT:
				return "crit";
			case MAGIC_CRIT:
				return "magicCrit";
			case SMOKE:
				return "smoke";
			case MOB_SPELL:
				return "mobSpell";
			case MOB_SPELL_AMBIENT:
				return "mobSpellAmbient";
			case SPELL:
				return "spell";
			case INSTANT_SPELL:
				return "instantSpell";
			case WITCH_MAGIC:
				return "witchMagic";
			case NOTE:
				return "note";
			case PORTAL:
				return "portal";
			case ENCHANTMENT_TABLE:
				return "enchantmenttable";
			case EXPLODE:
				return "explode";
			case FLAME:
				return "flame";
			case LAVA:
				return "lava";
			case FOOTSTEP:
				return "footstep";
			case SPLASH:
				return "splash";
			case WAKE:
				return "wake";
			case LARGE_SMOKE:
				return "largesmoke";
			case CLOUD:
				return "cloud";
			case RED_DUST:
				return "reddust";
			case SNOWBALL_POOF:
				return "snowballpoof";
			case DRIP_WATER:
				return "dripWater";
			case DRIP_LAVA:
				return "dripLava";
			case SNOW_SHOVEL:
				return "snowshovel";
			case SLIME:
				return "slime";
			case HEART:
				return "heart";
			case ANGRY_VILLAGER:
				return "angryVillager";
			case HAPPY_VILLAGER:
				return "happyVillager";
			default:
				throw new IllegalArgumentException("Unsupported Particle " + particle.name() + "!");
		}
	}

	@SuppressWarnings("deprecation")
	private static String getParticleName(final MaterialParticleType type, final Material material, final byte data)
	{
		switch (type)
		{
			case ICON_BREAK:
				return "iconcrack_" + material.getId() + "_" + data;
			case BLOCK_BREAK:
				return "blockcrack_" + material.getId() + "_" + data;
			case BLOCK_DUST:
				return "blockdust_" + material.getId() + "_" + data;
			default:
				throw new IllegalArgumentException("Unsupported MaterialParticleType " + type.name() + "!");
		}
	}
}
