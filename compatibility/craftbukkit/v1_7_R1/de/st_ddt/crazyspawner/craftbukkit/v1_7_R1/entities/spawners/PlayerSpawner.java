package de.st_ddt.crazyspawner.craftbukkit.v1_7_R1.entities.spawners;

import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.st_ddt.crazyspawner.craftbukkit.v1_7_R1.entities.spawners.players.NPCHelper;
import de.st_ddt.crazyutil.entities.ConfigurableEntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public final class PlayerSpawner implements NamedEntitySpawner, ConfigurableEntitySpawner
{

	protected final String name;

	public PlayerSpawner(final String name)
	{
		super();
		this.name = name;
	}

	public PlayerSpawner(final ConfigurationSection config)
	{
		super();
		this.name = null;
	}

	public PlayerSpawner(final Map<String, ? extends Paramitrisable> params)
	{
		super();
		this.name = null;
	}

	@Override
	public String getName()
	{
		return "PLAYER";
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.PLAYER;
	}

	@Override
	public Class<? extends Entity> getEntityClass()
	{
		return Player.class;
	}

	@Override
	public Set<Class<?>> getEntityClasses()
	{
		return null;
	}

	@Override
	public EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.SPECIAL;
	}

	@Override
	public Entity spawn(final Location location)
	{
		return NPCHelper.spawnPlayer(location, name);
	}

	@Override
	public boolean matches(final Entity entity)
	{
		if (!(entity instanceof Player))
			return false;
		else
			return ((Player) entity).getName().equals(name) && entity.hasMetadata("NPC");
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		// TODO Auto-generated method stub
	}
}
