package de.st_ddt.crazyutil.entities;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LightningStrike;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableSet;

import de.st_ddt.crazyspawner.entities.properties.EntityPropertyHelper;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyInterface;
import de.st_ddt.crazyspawner.entities.spawners.CustomizedParentedSpawner;
import de.st_ddt.crazyspawner.entities.spawners.NamedParentedSpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MaterialParamitriable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public class EntitySpawnerHelper extends EntityMatcherHelper
{

	protected final static EntitySpawner[] ENTITYSPAWNERS = EntitySpawner.ENTITYSPAWNERS;
	protected final static String[] NICEENTITYNAMES = new String[ENTITYSPAWNERS.length];
	static
	{
		// Spawner - Default
		for (final EntityType type : EntityType.values())
			if (type.isSpawnable())
				registerEntitySpawner(new DefaultSpawner(type));
		// Spawner - Fixes
		registerEntitySpawner(new ClassSpawner(EntityType.EGG));
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

			private final ItemStack item = new ItemStack(Material.STONE);

			@Override
			public Entity spawn(final Location location)
			{
				return location.getWorld().dropItem(location, item);
			}
		});
		registerEntitySpawner(new FallingBlockSpawner());
		registerEntitySpawner(new ClassSpawner(EntityType.FIREWORK));
		registerEntitySpawner(new CenteredSpawner(EntityType.ITEM_FRAME)
		{

			@Override
			public Entity spawn(final Location location)
			{
				final Block block = location.getBlock();
				if (!block.getType().isSolid())
					block.setType(Material.STONE);
				final Entity entity = super.spawn(location);
				return entity;
			}
		});
		registerEntitySpawner(new CenteredSpawner(EntityType.LEASH_HITCH)
		{

			@Override
			public Entity spawn(final Location location)
			{
				final Block block = location.getBlock();
				switch (block.getType())
				{
					case FENCE:
					case NETHER_FENCE:
						break;
					default:
						block.setType(Material.FENCE);
				}
				final Entity entity = super.spawn(location);
				return entity;
			}
		});
		registerEntitySpawner(new LightningSpawner());
		registerEntitySpawner(new CenteredSpawner(EntityType.PAINTING)
		{

			@Override
			public Entity spawn(final Location location)
			{
				final Block block = location.getBlock();
				if (!block.getType().isSolid())
					block.setType(Material.STONE);
				final Entity entity = super.spawn(location);
				return entity;
			}
		});
		registerEntitySpawner(new ClassSpawner(EntityType.SPLASH_POTION));
		// Nice EntityNamed
		for (final EntityType type : EntityType.values())
			NICEENTITYNAMES[type.ordinal()] = convertToNiceName(type);
	}

	public static void registerEntitySpawner(final EntitySpawner spawner)
	{
		ENTITYSPAWNERS[spawner.getEntityType().ordinal()] = spawner;
	}

	public static EntitySpawner getSpawner(final EntityType type)
	{
		return ENTITYSPAWNERS[type.ordinal()];
	}

	public static Set<EntityType> getSpawnableEntityTypes()
	{
		final Set<EntityType> res = new TreeSet<EntityType>(new Comparator<EntityType>()
		{

			@Override
			public int compare(final EntityType o1, final EntityType o2)
			{
				return o1.name().compareTo(o2.name());
			}
		});
		for (final EntityType type : EntityType.values())
			if (ENTITYSPAWNERS[type.ordinal()] != null)
				res.add(type);
		return res;
	}

	public static int getTotalSpawnableEntityTypeCount()
	{
		return getSpawnableEntityTypes().size();
	}

	public static Collection<? extends Entity> getMatchingEntites(final World world, final EntitySpawner spawner)
	{
		return getMatchingEntites(world, spawner.getEntityType(), spawner);
	}

	public static Collection<? extends Entity> getMatchingEntites(final Location location, final double distance, final EntitySpawner spawner)
	{
		return getMatchingEntites(location, distance, spawner.getEntityType(), spawner);
	}

	public static String getEntityTypeName(final EntityType type)
	{
		if (type == null)
			return "MULTI";
		else
			return type.name();
	}

	public static String getNiceEntityTypeName(final EntityType type)
	{
		if (type == null)
			return "Multi";
		else
			return NICEENTITYNAMES[type.ordinal()];
	}

	/**
	 * Converts the UpperCase {@link EntityType#name()} to a nicer version.
	 * 
	 * @param type
	 *            The {@link EntityType} to convert.<br>
	 *            Must not be Null.
	 * @return The nice version of the {@link EntityType#name()}.
	 */
	private static String convertToNiceName(final EntityType type)
	{
		switch (type)
		{
			case PRIMED_TNT:
				return "Primed TNT";
			case MINECART_TNT:
				return "Minecart TNT";
			default:
				final char[] chars = type.name().toCharArray();
				boolean start = true;
				for (int i = 0; i < chars.length; i++)
					if (start)
						start = false;
					else
					{
						final char cha = chars[i];
						if (cha == '_')
						{
							chars[i] = ' ';
							start = true;
						}
						else
							chars[i] = Character.toLowerCase(cha);
					}
				return new String(chars);
		}
	}

	public static EntitySpawner loadParent(final ConfigurationSection config)
	{
		if (config == null)
			return null;
		final String spawnerType = config.getString("spawnerType", null);
		if (spawnerType == null)
			return loadLegacySpawner(config);
		final EntitySpawnerType type = EntitySpawnerType.valueOf(spawnerType);
		switch (type)
		{
			case RAW:
				return getRawSpawner(config);
			case SPECIAL:
				return getSpecialSpawner(config);
			case CONFIGURABLE:
				return new CustomizedParentedSpawner(config);
			case NAMED:
				return NamedEntitySpawnerHelper.getNamedEntitySpawner(config.getString("name", null));
			default:
				return null;
		}
	}

	public static void saveParentSpawner(final EntitySpawner spawner, final ConfigurationSection config, final String path)
	{
		final EntitySpawnerType spawnerType = spawner.getSpawnerType();
		config.set(path + "spawnerType", spawnerType.name());
		switch (spawnerType)
		{
			case RAW:
				config.set(path + "entityType", spawner.getEntityType().name());
				return;
			case SPECIAL:
				if (spawner instanceof ConfigurableEntitySpawner)
				{
					config.set(path + "entityType", spawner.getEntityType().name());
					((ConfigurableEntitySpawner) spawner).save(config, path);
					return;
				}
				else
					break;
			case NAMED:
				if (spawner instanceof NamedEntitySpawner)
				{
					config.set(path + "name", ((NamedEntitySpawner) spawner).getName());
					return;
				}
				else
					break;
			case CONFIGURABLE:
				if (spawner instanceof ConfigurableEntitySpawner)
				{
					((ConfigurableEntitySpawner) spawner).save(config, path);
					return;
				}
				else
					break;
			default:
				break;
		}
		throw new IllegalArgumentException("Corrupt Spawner {type: " + spawnerType.name() + "; class: " + spawner.getClass().getName() + "; info: " + spawner.toString() + "}");
	}

	public static EntitySpawner loadLegacySpawner(final ConfigurationSection config)
	{
		return new LegacyEntitySpawner(config).convert();
	}

	private static EntitySpawner getRawSpawner(final ConfigurationSection config)
	{
		final String entityTypeName = config.getString("entityType", null);
		if (entityTypeName == null)
			return null;
		final EntityType type = EntityType.valueOf(entityTypeName);
		if (type == null)
			return null;
		else
			return getSpawner(type);
	}

	private static EntitySpawner getSpecialSpawner(final ConfigurationSection config)
	{
		final EntitySpawner spawner = getRawSpawner(config);
		if (spawner instanceof ConfigurableEntitySpawner)
		{
			final Class<? extends EntitySpawner> spawnerClass = spawner.getClass();
			try
			{
				final Constructor<? extends EntitySpawner> spawnerConstructor = spawnerClass.getConstructor(ConfigurationSection.class);
				return spawnerConstructor.newInstance(config);
			}
			catch (final Exception e)
			{
				System.err.println("[CrazySpawner] WARNING: Serious Bug detected, please report this!");
				System.err.println("EntitySpawnerClass: " + spawnerClass.getName());
				e.printStackTrace();
				return null;
			}
		}
		else
			return spawner;
	}

	protected EntitySpawnerHelper()
	{
	}

	private abstract static class BasicSpawner implements NamedEntitySpawner
	{

		protected final EntityType type;
		protected final Set<Class<?>> classes;

		public BasicSpawner(final EntityType type)
		{
			if (type == null)
				throw new IllegalArgumentException("Type cannot be Null!");
			this.type = type;
			this.classes = ImmutableSet.copyOf(type.getEntityClass().getClasses());
		}

		@Override
		public String getName()
		{
			return "RAW_" + getEntityTypeName(type);
		}

		@Override
		public final EntityType getEntityType()
		{
			return type;
		}

		@Override
		public final Class<? extends Entity> getEntityClass()
		{
			return type.getEntityClass();
		}

		@Override
		public Set<Class<?>> getEntityClasses()
		{
			return classes;
		}

		@Override
		public abstract Entity spawn(Location location);

		@Override
		public boolean matches(final Entity entity)
		{
			return entity.getType() == type;
		}

		@Override
		public EntitySpawnerType getSpawnerType()
		{
			return EntitySpawnerType.RAW;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "}";
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
				return location.getWorld().spawn(location, getEntityClass());
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}

	public final static class FallingBlockSpawner extends BasicSpawner implements ConfigurableEntitySpawner
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

		public FallingBlockSpawner(final ConfigurationSection config)
		{
			super(EntityType.FALLING_BLOCK);
			this.material = Material.valueOf(config.getString("material", "STONE"));
			if (material == null)
				throw new IllegalArgumentException("Material cannot be null!");
			this.data = (byte) config.getInt("data", 0);
		}

		public FallingBlockSpawner(final Map<String, ? extends Paramitrisable> params)
		{
			super(EntityType.FALLING_BLOCK);
			this.material = ((MaterialParamitriable) params.get("material")).getValue();
			this.data = ((IntegerParamitrisable) params.get("data")).getValue().byteValue();
		}

		@Override
		public EntitySpawnerType getSpawnerType()
		{
			return EntitySpawnerType.SPECIAL;
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
		public boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof FallingBlock)
			{
				final FallingBlock block = (FallingBlock) entity;
				return block.getMaterial() == material;
			}
			else
				return false;
		}

		@Override
		public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
		{
			final MaterialParamitriable materialParam = new MaterialParamitriable(material);
			params.put("m", materialParam);
			params.put("mat", materialParam);
			params.put("material", materialParam);
			final IntegerParamitrisable dataParamitrisable = new IntegerParamitrisable(data);
			params.put("data", dataParamitrisable);
		}

		@Override
		public void save(final ConfigurationSection config, final String path)
		{
			config.set(path + "spawnerType", "SPECIAL");
			config.set(path + "spawner", getName());
			config.set(path + "material", material.name());
			config.set(path + "data", data);
		}
	}

	public final static class LightningSpawner extends BasicSpawner implements ConfigurableEntitySpawner
	{

		protected final boolean effect;

		public LightningSpawner()
		{
			super(EntityType.LIGHTNING);
			this.effect = false;
		}

		public LightningSpawner(final ConfigurationSection config)
		{
			super(EntityType.LIGHTNING);
			this.effect = config.getBoolean("effect", false);
		}

		public LightningSpawner(final Map<String, ? extends Paramitrisable> params)
		{
			super(EntityType.LIGHTNING);
			this.effect = ((BooleanParamitrisable) params.get("effect")).getValue();
		}

		public LightningSpawner(final boolean effect)
		{
			super(EntityType.LIGHTNING);
			this.effect = effect;
		}

		@Override
		public final String getName()
		{
			return "LIGHTNINGSTRIKE";
		}

		@Override
		public final LightningStrike spawn(final Location location)
		{
			if (effect)
				return location.getWorld().strikeLightningEffect(location);
			else
				return location.getWorld().strikeLightning(location);
		}

		@Override
		public final boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof LightningStrike)
			{
				final LightningStrike lightning = (LightningStrike) entity;
				return lightning.isEffect() == effect;
			}
			else
				return false;
		}

		@Override
		public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
		{
			final BooleanParamitrisable effectParam = new BooleanParamitrisable(effect);
			params.put("e", effectParam);
			params.put("effect", effectParam);
			params.put("lightningeffect", effectParam);
		}

		@Override
		public void save(final ConfigurationSection config, final String path)
		{
			config.set(path + "spawnerType", EntitySpawnerType.SPECIAL.name());
			config.set(path + "spawner", getName());
			config.set(path + "effect", effect);
		}
	}

	private final static class LegacyEntitySpawner
	{

		protected final String name;
		protected final EntityType type;
		protected final List<EntityPropertyInterface> properties;

		public LegacyEntitySpawner(final ConfigurationSection config)
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

		public NamedEntitySpawner convert()
		{
			final CustomizedParentedSpawner spawner = new CustomizedParentedSpawner(EntitySpawnerHelper.getSpawner(type), properties);
			return new NamedParentedSpawner(spawner, name);
		}
	}
}
