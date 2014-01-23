package de.st_ddt.crazyspawner.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyHelper;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyInterface;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.entities.ApplyableEntitySpawner;
import de.st_ddt.crazyutil.entities.ConfigurableEntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

@SerializableAs("CrazySpawner_CustomEntitySpawner")
public class CustomizedParentedSpawner extends BasicParentedSpawner implements ConfigurationSaveable, ApplyableEntitySpawner
{

	protected final List<EntityPropertyInterface> properties;

	public CustomizedParentedSpawner(final EntityType type)
	{
		super(type);
		this.properties = EntityPropertyHelper.getDefaultEntityProperties(getEntityType());
	}

	public CustomizedParentedSpawner(final EntitySpawner spawner)
	{
		super(spawner);
		this.properties = EntityPropertyHelper.getDefaultEntityProperties(getEntityType());
	}

	public CustomizedParentedSpawner(final EntitySpawner spawner, final List<EntityPropertyInterface> properties)
	{
		super(spawner);
		this.properties = properties;
	}

	public CustomizedParentedSpawner(final ConfigurationSection config)
	{
		super(config);
		this.properties = EntityPropertyHelper.getEntityPropertiesFromConfig(getEntityType(), config);
	}

	public CustomizedParentedSpawner(final EntityType type, final Map<String, ? extends Paramitrisable> params)
	{
		super(((NamedEntitySpawnerParamitrisable) params.get("template")).getValue());
		this.properties = EntityPropertyHelper.getEntityPropertiesFromParams(type, params);
	}

	/**
	 * Creates a CustomEntitySpawner from args.<br>
	 * This is a helper method for default custom entities.
	 * 
	 * @param name
	 *            The name of the custom entity.
	 * @param type
	 *            The entity type of this spawner.
	 * @param sender
	 *            The CommandSender how creates this object.
	 * @param args
	 *            The params to create this object.
	 */
	public CustomizedParentedSpawner(final NamedEntitySpawner spawner, final CommandSender sender, final String... args)
	{
		super(spawner);
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		EntityPropertyHelper.getCommandParams(getEntityType(), params, sender);
		for (final String arg : args)
		{
			final String[] split = arg.split(":", 2);
			final Paramitrisable param = params.get(split[0]);
			if (param != null)
				try
				{
					param.setParameter(split[1]);
				}
				catch (final CrazyException e)
				{
					e.printStackTrace();
				}
		}
		this.properties = EntityPropertyHelper.getEntityPropertiesFromParams(getEntityType(), params);
	}

	@Override
	public EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.CONFIGURABLE;
	}

	@Override
	public final Entity spawn(final Location location)
	{
		final Entity entity = super.spawn(location);
		if (entity == null)
			return null;
		apply(entity);
		return entity;
	}

	// @Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.NAME $Name$", "CRAZYSPAWNER.ENTITY.PROPERTY.TYPE $EntityType$" })
	// public void show(final CommandSender target)
	// {
	// CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.NAME", target, name);
	// CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.TYPE", target, type.name());
	// for (final EntityPropertyInterface property : properties)
	// property.show(target);
	// }
	/**
	 * Apply all features to the given entity.<br>
	 * EntityType of this Spawner must match the EntityType of the given entity.
	 * 
	 * @param entity
	 *            The entity the properties should be applied to.
	 */
	@Override
	public final void apply(final Entity entity)
	{
		for (final EntityPropertyInterface property : properties)
			property.apply(entity);
	}

	public final void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		for (final EntityPropertyInterface property : properties)
			property.getCommandParams(params, sender);
		if (spawner.getSpawnerType() == EntitySpawnerType.SPECIAL && spawner instanceof ConfigurableEntitySpawner)
			((ConfigurableEntitySpawner) spawner).getCommandParams(params, sender);
	}

	public final void addEntityProperty(final EntityPropertyInterface property)
	{
		if (property == null)
			return;
		for (int i = 0; i < properties.size(); i++)
			if (properties.get(i).getClass().getName().equals(property.getClass().getName()))
			{
				properties.set(i, property);
				return;
			}
		properties.add(property);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		for (final EntityPropertyInterface property : properties)
			property.save(config, path);
	}
}
