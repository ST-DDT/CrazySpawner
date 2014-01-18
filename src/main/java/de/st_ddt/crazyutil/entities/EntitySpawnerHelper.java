package de.st_ddt.crazyutil.entities;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LightningStrike;
import org.bukkit.inventory.ItemStack;

public class EntitySpawnerHelper extends EntityMatcherHelper
{

	protected final static EntitySpawner[] ENTITYSPAWNERS = EntitySpawner.ENTITYSPAWNERS;
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
	}

	public static void registerEntitySpawner(final EntitySpawner spawner)
	{
		ENTITYSPAWNERS[spawner.getType().ordinal()] = spawner;
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
		return getMatchingEntites(world, spawner.getType(), spawner);
	}

	public static Collection<? extends Entity> getMatchingEntites(final Location location, final double distance, final EntitySpawner spawner)
	{
		return getMatchingEntites(location, distance, spawner.getType(), spawner);
	}

	protected EntitySpawnerHelper()
	{
	}

	abstract static class BasicSpawner implements NamedEntitySpawner
	{

		protected final EntityType type;

		public BasicSpawner(final EntityType type)
		{
			if (type == null)
				throw new IllegalArgumentException("Type cannot be Null!");
			this.type = type;
		}

		@Override
		public final EntityType getType()
		{
			return type;
		}

		@SuppressWarnings("deprecation")
		@Override
		public String getName()
		{
			if (type.getName() == null)
				return type.name();
			else
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
		public boolean matches(final Entity entity)
		{
			return entity.getType() == type;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "}";
		}
	}

	static class DefaultSpawner extends BasicSpawner
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

	static class CenteredSpawner extends DefaultSpawner
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

	static class ClassSpawner extends DefaultSpawner
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
	}

	public static class LightningSpawner extends DefaultSpawner
	{

		protected final boolean effect;

		public LightningSpawner()
		{
			super(EntityType.LIGHTNING);
			this.effect = false;
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
	}
}
