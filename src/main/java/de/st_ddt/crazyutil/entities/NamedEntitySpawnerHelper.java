package de.st_ddt.crazyutil.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.meta.FireworkMeta;

import de.st_ddt.crazyspawner.entities.spawners.BasicParentedSpawner;
import de.st_ddt.crazyutil.RandomUtil;

@SuppressWarnings("deprecation")
public class NamedEntitySpawnerHelper extends EntitySpawnerHelper
{

	protected final static Map<String, NamedEntitySpawner> NAMEDENTITYSPAWNERS = NamedEntitySpawner.NAMEDENTITYSPAWNERS;
	static
	{
		// Add Spawners to NamedEntitySpawnerParamitrisable
		for (final EntitySpawner spawner : EntitySpawnerHelper.ENTITYSPAWNERS)
			if (spawner != null)
				if (spawner instanceof NamedEntitySpawner)
					registerNamedEntitySpawner((NamedEntitySpawner) spawner, spawner.getEntityType().name(), spawner.getEntityType().getName());
		// register age spawners
		for (final EntityType type : EntityType.values())
			if (type.isAlive() && type.isSpawnable())
				if (Ageable.class.isAssignableFrom(type.getEntityClass()))
					registerNamedEntitySpawner(new BabySpawner(type));
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "BABY_ZOMBIE";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Zombie zombie = (Zombie) entity;
				zombie.setBaby(true);
			}
		});
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "BABY_ZOMBIE_VILLAGER";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Zombie zombie = (Zombie) entity;
				zombie.setVillager(true);
				zombie.setBaby(true);
			}
		});
		// Creeper
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.CREEPER)
		{

			@Override
			public String getName()
			{
				return "UNPOWEREDCREEPER";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Creeper creeper = (Creeper) entity;
				creeper.setPowered(false);
			}
		}, "UNCHARGEDCREEPER");
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.CREEPER)
		{

			@Override
			public String getName()
			{
				return "POWEREDCREEPER";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Creeper creeper = (Creeper) entity;
				creeper.setPowered(true);
			}
		}, "CHARGEDCREEPER");
		// Firework
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.FIREWORK)
		{

			private final Random RANDOM = new Random();

			@Override
			public String getName()
			{
				return "RANDOM_FIREWORK";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Firework firework = (Firework) entity;
				final FireworkMeta meta = (FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK);
				meta.setPower(RANDOM.nextInt(4) + 1);
				do
					meta.addEffect(getRandomEffect());
				while (RANDOM.nextBoolean());
				firework.setFireworkMeta(meta);
			}

			public FireworkEffect getRandomEffect()
			{
				final FireworkEffect.Builder effect = FireworkEffect.builder();
				effect.flicker(RANDOM.nextBoolean());
				effect.trail(RANDOM.nextBoolean());
				effect.with(getRandomType());
				do
					effect.withColor(getRandomColor());
				while (RANDOM.nextBoolean());
				while (RANDOM.nextBoolean())
					effect.withFade(getRandomColor());
				return effect.build();
			}

			public FireworkEffect.Type getRandomType()
			{
				return RandomUtil.randomElement(FireworkEffect.Type.values());
			}

			public Color getRandomColor()
			{
				return Color.fromRGB(RANDOM.nextInt(0xFFFFFF));
			}
		});
		// Horse
		for (final Horse.Color color : Horse.Color.values())
			registerNamedEntitySpawner(new HorseColorSpawner(color));
		for (final Horse.Variant variant : Horse.Variant.values())
			registerNamedEntitySpawner(new HorseVariantSpawner(variant));
		// Ocelot
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.OCELOT)
		{

			@Override
			public String getName()
			{
				return "CAT";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Ocelot ocelot = (Ocelot) entity;
				ocelot.setTamed(true);
			}
		});
		// Pig Zombie
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.PIG_ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "ANGRY_PIG_ZOMBIE";
			}

			@Override
			public void apply(final Entity entity)
			{
				final PigZombie pigzombie = (PigZombie) entity;
				pigzombie.setAngry(true);
			}
		}, "ANGRY_PIGMEN", "ANGRYPIG_ZOMBIE", "ANGRYPIGMEN");
		// Player
		// registerNamedEntitySpawner(new RandomPlayerSpawner()); via CrazySpawner.initialize()
		// Sheep
		for (final DyeColor color : DyeColor.values())
			registerNamedEntitySpawner(new SheepSpawner(color));
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.SHEEP)
		{

			@Override
			public String getName()
			{
				return "RANDOM_COLORED_SHEEP";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Sheep sheep = (Sheep) entity;
				sheep.setColor(RandomUtil.randomElement(DyeColor.values()));
			}
		}, "RANDOM_SHEEP");
		// Skeleton
		for (final Skeleton.SkeletonType variant : Skeleton.SkeletonType.values())
			registerNamedEntitySpawner(new SkeletonSpawner(variant), variant.name() + EntityType.SKELETON.name());
		// Slime
		for (int i = 1; i <= SlimeSpawner.SIZECOUNT; i++)
		{
			final String sizeText = SlimeSpawner.getSizeText(i);
			final SlimeSpawner slimeType = new SlimeSpawner(EntityType.SLIME, i);
			registerNamedEntitySpawner(slimeType, sizeText + "SLIME");
			final SlimeSpawner magmaType = new SlimeSpawner(EntityType.MAGMA_CUBE, i);
			registerNamedEntitySpawner(magmaType, sizeText + "LAVASLIME", sizeText + "MAGMACUBE", sizeText + "_MAGMACUBE");
		}
		// Villager
		for (final Villager.Profession profession : Villager.Profession.values())
			registerNamedEntitySpawner(new VillagerSpawner(profession));
		// Wolf
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.WOLF)
		{

			@Override
			public String getName()
			{
				return "ANGRY_WOLF";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Wolf wolf = (Wolf) entity;
				wolf.setAngry(true);
			}
		}, "ANGRYWOLF");
		// Zombie
		registerNamedEntitySpawner(new DefaultSpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "ZOMBIE_VILLAGER";
			}

			@Override
			public void apply(final Entity entity)
			{
				final Zombie zombie = (Zombie) entity;
				zombie.setVillager(true);
			}
		});
	}

	public static void registerNamedEntitySpawner(final NamedEntitySpawner entitySpawner, final String... aliases)
	{
		if (entitySpawner == null)
			throw new IllegalArgumentException("EntitySpawner cannot be null!");
		if (entitySpawner.getName() == null)
			throw new IllegalArgumentException("EntitySpawner's name cannot be null (" + entitySpawner + ")!");
		NAMEDENTITYSPAWNERS.put(entitySpawner.getName().toUpperCase(), entitySpawner);
		for (final String alias : aliases)
			if (alias != null)
				NAMEDENTITYSPAWNERS.put(alias.toUpperCase(), entitySpawner);
	}

	public static NamedEntitySpawner getNamedEntitySpawner(final String name)
	{
		if (name == null)
			return null;
		else
			return NAMEDENTITYSPAWNERS.get(name.toUpperCase());
	}

	public static List<NamedEntitySpawner> getNamedEntitySpawners(final EntityType type)
	{
		final List<NamedEntitySpawner> res = new LinkedList<NamedEntitySpawner>(NAMEDENTITYSPAWNERS.values());
		if (type == null)
			return res;
		else
		{
			final Iterator<NamedEntitySpawner> it = res.iterator();
			while (it.hasNext())
				if (it.next().getEntityType() != type)
					it.remove();
			return res;
		}
	}

	public static List<NamedEntitySpawner> getNamedEntitySpawnerList(final Collection<String> names)
	{
		if (names == null)
			return new ArrayList<NamedEntitySpawner>(0);
		final List<NamedEntitySpawner> res = new ArrayList<NamedEntitySpawner>(names.size());
		for (final String name : names)
			res.add(getNamedEntitySpawner(name));
		return res;
	}

	protected NamedEntitySpawnerHelper()
	{
	}

	private static abstract class DefaultSpawner extends BasicParentedSpawner implements NamedApplyableEntitySpawner
	{

		public DefaultSpawner(final EntityType type)
		{
			super(type);
		}

		public final String getParentName()
		{
			return getEntityTypeName(getEntityType());
		}

		@Override
		public final EntitySpawnerType getSpawnerType()
		{
			return EntitySpawnerType.NAMED;
		}

		@Override
		public final Entity spawn(final Location location)
		{
			final Entity entity = super.spawn(location);
			apply(entity);
			return entity;
		}
	}

	private final static class BabySpawner extends DefaultSpawner
	{

		public BabySpawner(final EntityType type)
		{
			super(type);
		}

		@Override
		public String getName()
		{
			return "BABY_" + getParentName();
		}

		@Override
		public void apply(final Entity entity)
		{
			final Ageable ageable = (Ageable) entity;
			ageable.setBaby();
		}

		@Override
		public boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof Ageable)
			{
				final Ageable ageable = (Ageable) entity;
				return !ageable.isAdult();
			}
			else
				return false;
		}
	}

	private final static class HorseColorSpawner extends DefaultSpawner
	{

		private final Horse.Color color;

		public HorseColorSpawner(final Horse.Color color)
		{
			super(EntityType.HORSE);
			this.color = color;
		}

		@Override
		public String getName()
		{
			return color.name() + "_" + getParentName();
		}

		@Override
		public void apply(final Entity entity)
		{
			final Horse horse = (Horse) entity;
			horse.setColor(color);
		}

		@Override
		public boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof Horse)
			{
				final Horse horse = (Horse) entity;
				return horse.getColor() == color;
			}
			else
				return false;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: HORSE; color: " + color.name() + "}";
		}
	}

	private final static class HorseVariantSpawner extends DefaultSpawner
	{

		private final Horse.Variant variant;

		public HorseVariantSpawner(final Horse.Variant variant)
		{
			super(EntityType.HORSE);
			this.variant = variant;
		}

		@Override
		public String getName()
		{
			return variant.name();
		}

		@Override
		public void apply(final Entity entity)
		{
			final Horse horse = (Horse) entity;
			horse.setVariant(variant);
		}

		@Override
		public boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof Entity)
			{
				final Horse horse = (Horse) entity;
				return horse.getVariant() == variant;
			}
			else
				return false;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: HORSE; variant: " + variant.name() + "}";
		}
	}

	// Sheep
	private final static class SheepSpawner extends DefaultSpawner
	{

		private final DyeColor color;

		public SheepSpawner(final DyeColor color)
		{
			super(EntityType.SHEEP);
			this.color = color;
		}

		@Override
		public String getName()
		{
			return color.name() + "_SHEEP";
		}

		@Override
		public void apply(final Entity entity)
		{
			final Sheep sheep = (Sheep) entity;
			sheep.setColor(color);
		}

		@Override
		public boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof Entity)
			{
				final Sheep sheep = (Sheep) entity;
				return sheep.getColor() == color;
			}
			else
				return false;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: SHEEP; color: " + color.name() + "}";
		}
	}

	// Skeleton
	private final static class SkeletonSpawner extends DefaultSpawner
	{

		private final Skeleton.SkeletonType variant;

		public SkeletonSpawner(final Skeleton.SkeletonType variant)
		{
			super(EntityType.SKELETON);
			this.variant = variant;
		}

		@Override
		public String getName()
		{
			return variant.name() + "_" + getParentName();
		}

		@Override
		public void apply(final Entity entity)
		{
			final Skeleton skeleton = (Skeleton) entity;
			skeleton.setSkeletonType(variant);
		}

		@Override
		public boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof Entity)
			{
				final Skeleton skeleton = (Skeleton) entity;
				return skeleton.getSkeletonType() == variant;
			}
			else
				return false;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: SKELETON; variant: " + variant.name() + "}";
		}
	}

	// Slime
	private static class SlimeSpawner extends DefaultSpawner
	{

		private final static String[] SIZES = new String[] { "TINY", "SMALL", "DEFAULT", "LARGE", "HUGE", "TINYGIANT", "SMALLGIANT", "GIANT", "LARGEGIANT", "HUGEGIANT" };
		private final static int SIZECOUNT = SIZES.length;
		private final int size;

		public static String getSizeText(final int size)
		{
			return SIZES[size - 1];
		}

		public SlimeSpawner(final EntityType slimeType, final int size)
		{
			super(slimeType);
			this.size = size;
		}

		@Override
		public String getName()
		{
			return getSizeText() + "_" + getParentName();
		}

		public final String getSizeText()
		{
			return getSizeText(size);
		}

		@Override
		public void apply(final Entity entity)
		{
			final Slime slime = (Slime) entity;
			slime.setSize(size);
		}

		@Override
		public boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof Entity)
			{
				final Slime slime = (Slime) entity;
				return slime.getSize() == size;
			}
			else
				return false;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: SLIME; size: " + size + "}";
		}
	}

	// Villager
	private final static class VillagerSpawner extends DefaultSpawner
	{

		private final Villager.Profession variant;

		public VillagerSpawner(final Villager.Profession variant)
		{
			super(EntityType.VILLAGER);
			this.variant = variant;
		}

		@Override
		public String getName()
		{
			return variant.toString();
		}

		@Override
		public void apply(final Entity entity)
		{
			final Villager villager = (Villager) entity;
			villager.setProfession(variant);
		}

		@Override
		public boolean matches(final Entity entity)
		{
			if (super.matches(entity) && entity instanceof Entity)
			{
				final Villager villager = (Villager) entity;
				return villager.getProfession() == variant;
			}
			else
				return false;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: VILLAGER; profession: " + variant.name() + "}";
		}
	}
}
