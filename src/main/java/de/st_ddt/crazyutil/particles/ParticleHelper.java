package de.st_ddt.crazyutil.particles;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.st_ddt.crazyspawner.CrazySpawner;

public final class ParticleHelper
{

	private ParticleHelper()
	{
	}

	private static ParticleHelperInterface particleHelper;

	/**
	 * This method initializes this ParticleHelper with a matching implementation of the required interface.<br>
	 * This method will be called by {@link CrazySpawner#initialize()}.
	 * 
	 * @return
	 */
	public static boolean initialize()
	{
		for (final Class<? extends ParticleHelperInterface> clazz : ParticleHelperInterface.PARTICLEHELPERCLASSES)
			try
			{
				particleHelper = clazz.newInstance();
				return true;
			}
			catch (final Throwable t)
			{}
		return false;
	}

	public static void castParticle(final Particle particle, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount)
	{
		particleHelper.castParticle(particle, location, offsetX, offsetY, offsetZ, speed, amount);
	}

	public static void castParticle(final Particle particle, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Player... players)
	{
		particleHelper.castParticle(particle, location, offsetX, offsetY, offsetZ, speed, amount, players);
	}

	public static void castParticle(final Particle particle, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Collection<Player> players)
	{
		particleHelper.castParticle(particle, location, offsetX, offsetY, offsetZ, speed, amount, players);
	}

	public static void castMaterialParticle(final MaterialParticleType type, final Material material, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount)
	{
		particleHelper.castMaterialParticle(type, material, data, location, offsetX, offsetY, offsetZ, speed, amount);
	}

	public static void castMaterialParticle(final MaterialParticleType type, final Material material, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Player... players)
	{
		particleHelper.castMaterialParticle(type, material, data, location, offsetX, offsetY, offsetZ, speed, amount, players);
	}

	public static void castMaterialParticle(final MaterialParticleType type, final Material material, final byte data, final Location location, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Collection<Player> players)
	{
		particleHelper.castMaterialParticle(type, material, data, location, offsetX, offsetY, offsetZ, speed, amount, players);
	}
}
