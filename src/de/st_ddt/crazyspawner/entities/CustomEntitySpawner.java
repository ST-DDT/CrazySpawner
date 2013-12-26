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
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.persistance.PersistanceManager;
import de.st_ddt.crazyspawner.entities.persistance.PersistantState;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyHelper;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyInterface;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.entities.EntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

@SuppressWarnings("deprecation")
@SerializableAs("CrazySpawner_CustomEntitySpawner")
public class CustomEntitySpawner implements NamedEntitySpawner, MetadataValue, ConfigurationSaveable, PersistantState
{

	public final static String METAHEADER = "CustomEntityMeta";
	public final static String PERSISTENCEKEY = "SPAWNER";
	static
	{
		PersistanceManager.registerPersistableState(CustomEntitySpawner.class);
	}
	protected final String name;
	protected final EntityType type;
	protected final List<EntityPropertyInterface> properties;

	public CustomEntitySpawner(final EntityType type)
	{
		this(type.getName() == null ? type.name() : type.getName(), type);
	}

	public CustomEntitySpawner(final String name, final EntityType type)
	{
		super();
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null!");
		if (name.length() == 0)
			throw new IllegalArgumentException("Name cannot be empty!");
		this.name = name.toUpperCase();
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.type = type;
		this.properties = EntityPropertyHelper.getDefaultEntityProperties(type);
	}

	public CustomEntitySpawner(final ConfigurationSection config)
	{
		super();
		if (config == null)
			throw new IllegalArgumentException("Config cannot be null!");
		this.name = config.getString("name", "UNNAMED").toUpperCase();
		final String typeName = config.getString("type", null);
		if (typeName == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.type = EntityType.valueOf(typeName.toUpperCase());
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.properties = EntityPropertyHelper.getEntityPropertiesFromConfig(type, config);
	}

	public CustomEntitySpawner(final EntityType type, final Map<String, ? extends Paramitrisable> params)
	{
		super();
		final StringParamitrisable nameParam = (StringParamitrisable) params.get("name");
		this.name = nameParam.getValue().toUpperCase();
		if (type == null)
			throw new IllegalArgumentException("EntityType cannot be null!");
		this.type = type;
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
	public CustomEntitySpawner(final String name, final EntityType type, final CommandSender sender, final String... args)
	{
		super();
		this.name = name;
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.type = type;
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		EntityPropertyHelper.getCommandParams(type, params, sender);
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
		this.properties = EntityPropertyHelper.getEntityPropertiesFromParams(type, params);
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final EntityType getType()
	{
		return type;
	}

	@Override
	public final Class<? extends Entity> getEntityClass()
	{
		return type.getEntityClass();
	}

	protected EntitySpawner getSpawner()
	{
		if (!properties.isEmpty())
		{
			final EntityPropertyInterface property = properties.get(0);
			if (property instanceof EntitySpawner)
				return (EntitySpawner) property;
		}
		return EntitySpawner.ENTITYSPAWNERS[type.ordinal()];
	}

	public final boolean isSpawnable()
	{
		return getSpawner() != null;
	}

	@Override
	public final Entity spawn(final Location location)
	{
		final EntitySpawner spawner = getSpawner();
		if (spawner == null)
			return null;
		final Entity entity = spawner.spawn(location);
		if (entity == null)
			return null;
		apply(entity);
		return entity;
	}

	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.NAME $Name$", "CRAZYSPAWNER.ENTITY.PROPERTY.TYPE $EntityType$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.NAME", target, name);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.TYPE", target, type.name());
		for (final EntityPropertyInterface property : properties)
			property.show(target);
	}

	/**
	 * Apply all features to the given entity.<br>
	 * EntityType of this Spawner must match the EntityType of the given entity.
	 * 
	 * @param entity
	 *            The entity the properties should be applied to.
	 */
	public final void apply(final Entity entity)
	{
		attachTo(entity, CrazySpawner.getPlugin().getPersistanceManager());
		for (final EntityPropertyInterface property : properties)
			property.apply(entity);
	}

	@Override
	public boolean matches(final Entity entity)
	{
		for (final MetadataValue meta : entity.getMetadata(METAHEADER))
			if (equals(meta))
				return true;
		return false;
	}

	public final StringParamitrisable getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final StringParamitrisable nameParam = new StringParamitrisable(name);
		params.put("n", nameParam);
		params.put("name", nameParam);
		for (final EntityPropertyInterface property : properties)
			property.getCommandParams(params, sender);
		return nameParam;
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
		config.set(path + "name", name.toUpperCase());
		config.set(path + "type", type.name());
		for (final EntityPropertyInterface property : properties)
			property.save(config, path);
	}

	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "name", "String");
		config.set(path + "type", "EntityType");
		for (final EntityPropertyInterface property : properties)
			property.dummySave(config, path);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		else if (obj instanceof CustomEntitySpawner)
			return name.equals(((CustomEntitySpawner) obj).name);
		else
			return false;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public final CustomEntitySpawner value()
	{
		return this;
	}

	@Override
	public final int asInt()
	{
		return 0;
	}

	@Override
	public final float asFloat()
	{
		return 0;
	}

	@Override
	public final double asDouble()
	{
		return 0;
	}

	@Override
	public final long asLong()
	{
		return 0;
	}

	@Override
	public final short asShort()
	{
		return 0;
	}

	@Override
	public final byte asByte()
	{
		return 0;
	}

	@Override
	public final boolean asBoolean()
	{
		return false;
	}

	@Override
	public final String asString()
	{
		return toString();
	}

	@Override
	public final CrazySpawner getOwningPlugin()
	{
		return CrazySpawner.getPlugin();
	}

	@Override
	public final void invalidate()
	{
	}

	@Override
	public String toString()
	{
		return "CustomEntitySpawner{name: " + getName() + "; type: " + type.name() + "}";
	}

	public static CustomEntitySpawner deserialize(final Map<String, Object> map)
	{
		return CrazySpawner.getPlugin().getCustomEntities().get(map.get("name"));
	}

	@Override
	public Map<String, Object> serialize()
	{
		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("name", name);
		return res;
	}

	@Override
	public void attachTo(final Entity entity, final PersistanceManager manager)
	{
		entity.setMetadata(METAHEADER, this);
		manager.watch(entity, PERSISTENCEKEY, this);
	}
}
