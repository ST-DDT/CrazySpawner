package de.st_ddt.crazyspawner.entities.spawners;

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
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyHelper;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyInterface;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.entities.ApplyableEntitySpawner;
import de.st_ddt.crazyutil.entities.ChangeableConfigurableEntitySpawner;
import de.st_ddt.crazyutil.entities.ConfigurableEntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.entities.ShowableEntitySpawner;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

@SerializableAs("CrazySpawner_CustomEntitySpawner")
public class CustomizedParentedSpawner extends BasicParentedSpawner implements ChangeableConfigurableEntitySpawner, ShowableEntitySpawner, ConfigurationSaveable, ApplyableEntitySpawner
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
		this.properties = EntityPropertyHelper.getDefaultEntityProperties(spawner);
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

	public CustomizedParentedSpawner(final EntitySpawner spawner, final Map<String, ? extends Paramitrisable> params)
	{
		super(spawner);
		this.properties = EntityPropertyHelper.getEntityPropertiesFromParams(spawner, params);
	}

	/**
	 * Creates a CustomEntitySpawner from args.<br>
	 * This is a helper method for default custom entities.
	 * 
	 * @param type
	 *            The entity type of this spawner.
	 * @param sender
	 *            The CommandSender how creates this object.
	 * @param args
	 *            The params to create this object.
	 */
	public CustomizedParentedSpawner(final EntityType type, final CommandSender sender, final String... args)
	{
		this(EntitySpawnerHelper.getSpawner(type), sender, args);
	}

	/**
	 * Creates a CustomEntitySpawner from args.<br>
	 * This is a helper method for default custom entities.
	 * 
	 * @param spawner
	 *            The entity type of this spawner.
	 * @param sender
	 *            The CommandSender how creates this object.
	 * @param args
	 *            The params to create this object.
	 */
	public CustomizedParentedSpawner(final EntitySpawner spawner, final CommandSender sender, final String... args)
	{
		super(spawner);
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		EntityPropertyHelper.getCommandParams(spawner, params, sender);
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

	@Override
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

	public void dummySave(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		for (final EntityPropertyInterface property : properties)
			property.dummySave(config, path);
	}

	@Override
	public CustomizedParentedSpawner change(final Map<String, ? extends TabbedParamitrisable> params)
	{
		return new CustomizedParentedSpawner(spawner, params);
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.SHOW.CUSTOMIZEDPARENTED {EntityType}", "CRAZYSPAWNER.ENTITY.SHOW.PARENT", "CRAZYSPAWNER.ENTITY.SHOW.PARENT.NAMED {ParentName}", "CRAZYSPAWNER.ENTITY.SHOW.PARENT.UNKNOWN {ToString}" })
	public void show(final CommandSender target)
	{
		final CrazySpawner plugin = CrazySpawner.getPlugin();
		plugin.sendLocaleMessage("ENTITY.SHOW.CUSTOMIZEDPARENTED", target, EntitySpawnerHelper.getNiceEntityTypeName(getEntityType()));
		plugin.sendLocaleMessage("ENTITY.SHOW.PARENT", target);
		for (final EntityPropertyInterface property : properties)
			property.show(target);
		if (spawner instanceof ShowableEntitySpawner)
			((ShowableEntitySpawner) spawner).show(target);
		else if (spawner instanceof NamedEntitySpawner)
			plugin.sendLocaleMessage("ENTITY.SHOW.PARENT.NAMED", target, ((NamedEntitySpawner) spawner).getName());
		else
			plugin.sendLocaleMessage("ENTITY.SHOW.PARENT.UNKNOWN", target, spawner.toString());
	}
}
