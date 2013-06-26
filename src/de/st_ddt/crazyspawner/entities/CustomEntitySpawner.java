package de.st_ddt.crazyspawner.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.properties.AgeProperty;
import de.st_ddt.crazyspawner.entities.properties.AlarmProperty;
import de.st_ddt.crazyspawner.entities.properties.BurningProperty;
import de.st_ddt.crazyspawner.entities.properties.ColorableProperty;
import de.st_ddt.crazyspawner.entities.properties.CreeperProperty;
import de.st_ddt.crazyspawner.entities.properties.DamageProperty;
import de.st_ddt.crazyspawner.entities.properties.DespawnProperty;
import de.st_ddt.crazyspawner.entities.properties.DetectionProperty;
import de.st_ddt.crazyspawner.entities.properties.DroppedItemProperty;
import de.st_ddt.crazyspawner.entities.properties.EndermanProperty;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyInterface;
import de.st_ddt.crazyspawner.entities.properties.EquipmentProperties;
import de.st_ddt.crazyspawner.entities.properties.ExperienceOrbProperty;
import de.st_ddt.crazyspawner.entities.properties.ExplosiveProperty;
import de.st_ddt.crazyspawner.entities.properties.FallingBlockProperties;
import de.st_ddt.crazyspawner.entities.properties.HealthProperty;
import de.st_ddt.crazyspawner.entities.properties.IronGolemProperty;
import de.st_ddt.crazyspawner.entities.properties.NameProperty;
import de.st_ddt.crazyspawner.entities.properties.OcelotProperty;
import de.st_ddt.crazyspawner.entities.properties.PassengerProperty;
import de.st_ddt.crazyspawner.entities.properties.PeacefulProperty;
import de.st_ddt.crazyspawner.entities.properties.PigProperty;
import de.st_ddt.crazyspawner.entities.properties.PigZombieProperty;
import de.st_ddt.crazyspawner.entities.properties.PotionProterty;
import de.st_ddt.crazyspawner.entities.properties.SheepProperty;
import de.st_ddt.crazyspawner.entities.properties.SkeletonProperty;
import de.st_ddt.crazyspawner.entities.properties.SlimeProperty;
import de.st_ddt.crazyspawner.entities.properties.TameableProperty;
import de.st_ddt.crazyspawner.entities.properties.VelocityProperty;
import de.st_ddt.crazyspawner.entities.properties.VillagerProperty;
import de.st_ddt.crazyspawner.entities.properties.WolfProperty;
import de.st_ddt.crazyspawner.entities.properties.XPProperty;
import de.st_ddt.crazyspawner.entities.properties.ZombieProperty;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.EntitySpawner;
import de.st_ddt.crazyutil.NamedEntitySpawner;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public final class CustomEntitySpawner implements NamedEntitySpawner, MetadataValue, ConfigurationSaveable
{

	public final static String METAHEADER = "CustomEntityMeta";
	protected final static boolean v146OrLater = VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.4.6") >= 0;
	protected final static boolean v150OrLater = VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.5.0") >= 0;
	protected final static EntitySpawner[] ENTITYSPAWNER = new EntitySpawner[EntityType.values().length];
	@SuppressWarnings("unchecked")
	protected final static Set<Class<? extends EntityPropertyInterface>>[] ENTITYPROPERTIES = new Set[EntityType.values().length];
	static
	{
		// Spawner - Default
		for (final EntityType type : EntityType.values())
			if (type.isSpawnable())
				registerEntitySpawner(new DefaultSpawner(type));
		// Spawner - Fixes
		registerEntitySpawner(new CenteredSpawner(EntityType.ENDER_CRYSTAL)
		{

			@Override
			public Entity spawn(final Location location)
			{
				final Entity entity = super.spawn(location);
				location.clone().add(0, 1, 0).getBlock().setType(Material.FIRE);
				location.getBlock().setType(Material.BEDROCK);
				return entity;
			}
		});
		registerEntitySpawner(new BasicSpawner(EntityType.DROPPED_ITEM)
		{

			private final ItemStack item = new ItemStack(1);

			@Override
			public Entity spawn(final Location location)
			{
				return location.getWorld().dropItem(location, item);
			}
		});
		registerEntitySpawner(new ClassSpawner(EntityType.FIREWORK));
		registerEntitySpawner(new FallingBlockSpawner());
		// Add Spawners to NamedEntitySpawnerParamitrisable
		for (final EntitySpawner spawner : ENTITYSPAWNER)
			if (spawner != null)
				if (spawner instanceof NamedEntitySpawner)
					NamedEntitySpawnerParamitrisable.registerNamedEntitySpawner((NamedEntitySpawner) spawner, spawner.getType().name(), spawner.getType().getName());
		// Properties
		for (final EntityType type : EntityType.values())
			ENTITYPROPERTIES[type.ordinal()] = new LinkedHashSet<Class<? extends EntityPropertyInterface>>();
		// Properties - VIP required to be first!
		registerEntityProperty(FallingBlockProperties.class, FallingBlock.class);
		// Properties - Sorted by EntityInterfaces
		registerEntityProperty(AgeProperty.class, Ageable.class);
		// Boat required?
		registerEntityProperty(ColorableProperty.class, Colorable.class);
		registerEntityProperty(AlarmProperty.class, Creature.class);
		registerEntityProperty(DetectionProperty.class, Creature.class);
		registerEntityProperty(CreeperProperty.class, Creeper.class);
		if (v146OrLater)
			registerEntityProperty(HealthProperty.class, Damageable.class);
		registerEntityProperty(EndermanProperty.class, Enderman.class);
		registerEntityProperty(BurningProperty.class, Entity.class);
		registerEntityProperty(VelocityProperty.class, Entity.class);
		registerEntityProperty(PassengerProperty.class, Entity.class);
		registerEntityProperty(PeacefulProperty.class, Entity.class);
		registerEntityProperty(ExperienceOrbProperty.class, ExperienceOrb.class);
		registerEntityProperty(ExplosiveProperty.class, Explosive.class);
		// Fireball required?
		// Firework impossible?
		// Hanging required?
		// TODO InventoryHolder
		registerEntityProperty(IronGolemProperty.class, IronGolem.class);
		registerEntityProperty(AlarmProperty.class, Item.class);
		registerEntityProperty(DroppedItemProperty.class, Item.class);
		// ItemFrame required?
		registerEntityProperty(DamageProperty.class, LivingEntity.class);
		registerEntityProperty(DespawnProperty.class, LivingEntity.class);
		registerEntityProperty(EquipmentProperties.class, LivingEntity.class);
		if (v150OrLater)
			registerEntityProperty(NameProperty.class, LivingEntity.class);
		registerEntityProperty(PotionProterty.class, LivingEntity.class);
		registerEntityProperty(XPProperty.class, LivingEntity.class);
		// Minecard required?
		registerEntityProperty(OcelotProperty.class, Ocelot.class);
		// Painting required?
		registerEntityProperty(PigProperty.class, Pig.class);
		registerEntityProperty(PigZombieProperty.class, PigZombie.class);
		// Projectile required?
		registerEntityProperty(SheepProperty.class, Sheep.class);
		registerEntityProperty(SkeletonProperty.class, Skeleton.class);
		registerEntityProperty(SlimeProperty.class, Slime.class);
		registerEntityProperty(TameableProperty.class, Tameable.class);
		// TODO TNTPrimed
		registerEntityProperty(VillagerProperty.class, Villager.class);
		registerEntityProperty(WolfProperty.class, Wolf.class);
		registerEntityProperty(ZombieProperty.class, Zombie.class);
	}

	public static void registerEntitySpawner(final EntitySpawner spawner)
	{
		ENTITYSPAWNER[spawner.getType().ordinal()] = spawner;
	}

	public static Set<EntityType> getSpawnableEntityTypes()
	{
		final Set<EntityType> res = new HashSet<EntityType>();
		for (final EntityType type : EntityType.values())
			if (ENTITYSPAWNER[type.ordinal()] != null)
				res.add(type);
		return res;
	}

	public static void registerEntityProperty(final Class<? extends EntityPropertyInterface> propertyClass, final Class<?> targetClass)
	{
		for (final EntityType type : EntityType.values())
			if (type.getEntityClass() != null && targetClass.isAssignableFrom(type.getEntityClass()))
				ENTITYPROPERTIES[type.ordinal()].add(propertyClass);
	}

	protected static List<EntityPropertyInterface> getDefaultEntityProperties(final EntityType type)
	{
		final Set<Class<? extends EntityPropertyInterface>> properties = ENTITYPROPERTIES[type.ordinal()];
		final List<EntityPropertyInterface> res = new ArrayList<EntityPropertyInterface>(properties.size());
		for (final Class<? extends EntityPropertyInterface> property : properties)
			try
			{
				res.add(property.newInstance());
			}
			catch (final Exception e)
			{
				System.err.println("WARNING: Serious Bug detected, please report this!");
				System.err.println("EntityType: " + type.name() + ", Property: " + property.getSimpleName());
				e.printStackTrace();
			}
		return res;
	}

	protected static List<EntityPropertyInterface> getEntityPropertiesFromConfig(final EntityType type, final ConfigurationSection config)
	{
		final Set<Class<? extends EntityPropertyInterface>> properties = ENTITYPROPERTIES[type.ordinal()];
		final List<EntityPropertyInterface> res = new ArrayList<EntityPropertyInterface>(properties.size());
		for (final Class<? extends EntityPropertyInterface> property : properties)
			try
			{
				res.add(property.getConstructor(ConfigurationSection.class).newInstance(config));
			}
			catch (final Exception e)
			{
				System.err.println("WARNING: Serious Bug detected, please report this!");
				System.err.println("EntityType: " + type.name() + ", Property: " + property.getSimpleName());
				e.printStackTrace();
			}
		return res;
	}

	protected static List<EntityPropertyInterface> getEntityPropertiesFromParams(final EntityType type, final Map<String, ? extends Paramitrisable> params)
	{
		final Set<Class<? extends EntityPropertyInterface>> properties = ENTITYPROPERTIES[type.ordinal()];
		final List<EntityPropertyInterface> res = new ArrayList<EntityPropertyInterface>(properties.size());
		for (final Class<? extends EntityPropertyInterface> property : properties)
			try
			{
				res.add(property.getConstructor(Map.class).newInstance(params));
			}
			catch (final Exception e)
			{
				System.err.println("WARNING: Serious Bug detected, please report this!");
				System.err.println("EntityType: " + type.name() + ", Property: " + property.getSimpleName());
				e.printStackTrace();
			}
		return res;
	}

	protected static StringParamitrisable getCommandParams(final EntityType type, final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final StringParamitrisable nameParam = new StringParamitrisable(null);
		params.put("n", nameParam);
		params.put("name", nameParam);
		for (final EntityPropertyInterface property : getDefaultEntityProperties(type))
			property.getCommandParams(params, sender);
		return nameParam;
	}

	protected final String name;
	protected final EntityType type;
	protected final List<EntityPropertyInterface> properties;

	public CustomEntitySpawner(final EntityType type)
	{
		this(type.getName(), type);
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
		this.properties = getDefaultEntityProperties(type);
	}

	public CustomEntitySpawner(final ConfigurationSection config)
	{
		super();
		this.name = config.getString("name", "UNNAMED").toUpperCase();
		final String typeName = config.getString("type", null);
		if (typeName == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.type = EntityType.valueOf(typeName.toUpperCase());
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.properties = getEntityPropertiesFromConfig(type, config);
	}

	public CustomEntitySpawner(final EntityType type, final Map<String, ? extends Paramitrisable> params)
	{
		super();
		final Paramitrisable nameParam = params.get("name");
		this.name = ((StringParamitrisable) nameParam).getValue().toUpperCase();
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.type = type;
		this.properties = getEntityPropertiesFromParams(type, params);
	}

	// EDIT Helper class for default custom entities
	public CustomEntitySpawner(final String name, final EntityType type, final CommandSender sender, final String... args)
	{
		super();
		this.name = name;
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.type = type;
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		getCommandParams(type, params, sender);
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
		this.properties = getEntityPropertiesFromParams(type, params);
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

	protected final EntitySpawner getSpawner()
	{
		if (!properties.isEmpty())
		{
			final EntityPropertyInterface property = properties.get(0);
			if (property instanceof EntitySpawner)
				return (EntitySpawner) property;
		}
		return ENTITYSPAWNER[type.ordinal()];
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
		entity.setMetadata(METAHEADER, this);
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
	public final void apply(final Entity entity)
	{
		for (final EntityPropertyInterface property : properties)
			property.apply(entity);
	}

	@Override
	public Collection<? extends Entity> getEntities(final World world)
	{
		// EDIT include entity properties or check meta
		return world.getEntitiesByClass(type.getEntityClass());
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

	private abstract static class BasicSpawner implements NamedEntitySpawner
	{

		protected final EntityType type;

		public BasicSpawner(final EntityType type)
		{
			this.type = type;
		}

		@Override
		public final EntityType getType()
		{
			return type;
		}

		@Override
		public String getName()
		{
			return type.getName();
		}

		@Override
		public final Class<? extends Entity> getEntityClass()
		{
			return type.getEntityClass();
		}

		@Override
		public abstract Entity spawn(Location location);

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			return world.getEntitiesByClass(type.getEntityClass());
		}
	}

	private static class DefaultSpawner extends BasicSpawner
	{

		public DefaultSpawner(final EntityType type)
		{
			super(type);
		}

		@Override
		public Entity spawn(final Location location)
		{
			return location.getWorld().spawnEntity(location, type);
		}
	}

	private static class CenteredSpawner extends DefaultSpawner
	{

		public CenteredSpawner(final EntityType type)
		{
			super(type);
		}

		@Override
		public Entity spawn(final Location location)
		{
			location.setX(Math.floor(location.getX()) + 0.5);
			location.setY(Math.floor(location.getY()));
			location.setZ(Math.floor(location.getZ()) + 0.5);
			location.setYaw(0);
			location.setPitch(0);
			return super.spawn(location);
		}
	}

	private static class ClassSpawner extends DefaultSpawner
	{

		public ClassSpawner(final EntityType type)
		{
			super(type);
		}

		@Override
		public Entity spawn(final Location location)
		{
			try
			{
				return location.getWorld().spawn(location, type.getEntityClass());
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}

	public static class FallingBlockSpawner extends DefaultSpawner
	{

		protected final Material material;
		protected final byte data;

		public FallingBlockSpawner()
		{
			super(EntityType.FALLING_BLOCK);
			this.material = Material.STONE;
			this.data = 0;
		}

		public FallingBlockSpawner(final Material material, final byte data)
		{
			super(EntityType.FALLING_BLOCK);
			if (material == null)
				throw new IllegalArgumentException("Material cannot be null!");
			this.material = material;
			this.data = data;
		}

		@Override
		public final FallingBlock spawn(final Location location)
		{
			try
			{
				return location.getWorld().spawnFallingBlock(location, material, data);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public final Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<FallingBlock> entities = world.getEntitiesByClass(FallingBlock.class);
			final Iterator<FallingBlock> it = entities.iterator();
			while (it.hasNext())
				if (it.next().getMaterial() != material)
					it.remove();
			return entities;
		}
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof CustomEntitySpawner)
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
	public CustomEntitySpawner value()
	{
		return this;
	}

	@Override
	public int asInt()
	{
		return 0;
	}

	@Override
	public float asFloat()
	{
		return 0;
	}

	@Override
	public double asDouble()
	{
		return 0;
	}

	@Override
	public long asLong()
	{
		return 0;
	}

	@Override
	public short asShort()
	{
		return 0;
	}

	@Override
	public byte asByte()
	{
		return 0;
	}

	@Override
	public boolean asBoolean()
	{
		return false;
	}

	@Override
	public String asString()
	{
		return toString();
	}

	@Override
	public CrazySpawner getOwningPlugin()
	{
		return CrazySpawner.getPlugin();
	}

	@Override
	public void invalidate()
	{
	}
}
