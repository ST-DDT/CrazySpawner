package de.st_ddt.crazyutil.particles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface ParticleHelperInterface
{

	public List<Class<? extends ParticleHelperInterface>> PARTICLEHELPERCLASSES = new ArrayList<>();

	public void castParticle(Particle particle, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount);

	public void castParticle(Particle particle, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players);

	public void castParticle(Particle particle, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, Collection<Player> players);

	public void castMaterialParticle(MaterialParticleType type, Material material, byte data, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount);

	public void castMaterialParticle(MaterialParticleType type, Material material, byte data, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players);

	public void castMaterialParticle(MaterialParticleType type, Material material, byte data, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount, Collection<Player> players);
}
