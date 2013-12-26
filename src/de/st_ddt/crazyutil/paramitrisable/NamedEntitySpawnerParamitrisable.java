package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.NamedEntitySpawner;

public class NamedEntitySpawnerParamitrisable extends TypedParamitrisable<NamedEntitySpawner>
{

	protected final static Map<String, NamedEntitySpawner> SPAWNERS = NamedEntitySpawner.SPAWNERS;
	static
	{
		// register default + age spawners
		for (final EntityType type : EntityType.values())
			if (type.isAlive() && type.isSpawnable())
			{
				registerNamedEntitySpawner(new DefaultNamedEntitySpawner(type), type.name());
				if (Ageable.class.isAssignableFrom(type.getEntityClass()))
					registerNamedEntitySpawner(new BabySpawner(type));
			}
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "BABY_ZOMBIE";
			}

			@Override
			public Zombie spawn(final Location location)
			{
				final Zombie zombie = (Zombie) super.spawn(location);
				zombie.setBaby(true);
				return zombie;
			}
		});
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "BABY_ZOMBIE_VILLAGER";
			}

			@Override
			public Zombie spawn(final Location location)
			{
				final Zombie zombie = (Zombie) super.spawn(location);
				zombie.setVillager(true);
				zombie.setBaby(true);
				return zombie;
			}
		});
		// Creeper
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.CREEPER)
		{

			@Override
			public String getName()
			{
				return "UNPOWEREDCREEPER";
			}

			@Override
			public Creeper spawn(final Location location)
			{
				final Creeper creeper = (Creeper) super.spawn(location);
				creeper.setPowered(false);
				return creeper;
			}
		}, "UNCHARGEDCREEPER");
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.CREEPER)
		{

			@Override
			public String getName()
			{
				return "POWEREDCREEPER";
			}

			@Override
			public Creeper spawn(final Location location)
			{
				final Creeper creeper = (Creeper) super.spawn(location);
				creeper.setPowered(true);
				return creeper;
			}
		}, "POWEREDCREEPER", "CHARGEDCREEPER");
		// Horse
		for (final Horse.Color color : Horse.Color.values())
			registerNamedEntitySpawner(new HorseColorSpawner(color));
		for (final Horse.Variant variant : Horse.Variant.values())
			registerNamedEntitySpawner(new HorseVariantSpawner(variant));
		// Ocelot
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.OCELOT)
		{

			@Override
			public String getName()
			{
				return "CAT";
			}

			@Override
			public Ocelot spawn(final Location location)
			{
				final Ocelot ocelot = (Ocelot) super.spawn(location);
				ocelot.setTamed(true);
				return ocelot;
			}
		});
		// Pig Zombie
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.PIG_ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "ANGRY_PIG_ZOMBIE";
			}

			@Override
			public PigZombie spawn(final Location location)
			{
				final PigZombie pigzombie = (PigZombie) super.spawn(location);
				pigzombie.setAngry(true);
				return pigzombie;
			}
		}, "ANGRY_PIGMEN", "ANGRYPIG_ZOMBIE", "ANGRYPIGMEN");
		// Sheep
		for (final DyeColor color : DyeColor.values())
			registerNamedEntitySpawner(new SheepSpawner(color));
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.SHEEP)
		{

			private final Random random = new Random();
			private final DyeColor[] colors = DyeColor.values();
			private final int max = colors.length;

			@Override
			public String getName()
			{
				return "RANDOM_COLORED_SHEEP";
			}

			@Override
			public Sheep spawn(final Location location)
			{
				final Sheep sheep = (Sheep) super.spawn(location);
				sheep.setColor(colors[random.nextInt(max)]);
				return sheep;
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
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.WOLF)
		{

			@Override
			public String getName()
			{
				return "ANGRY_WOLF";
			}

			@Override
			public Wolf spawn(final Location location)
			{
				final Wolf wolf = (Wolf) super.spawn(location);
				wolf.setAngry(true);
				return wolf;
			}
		}, "ANGRYWOLF");
		// Zombie
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "ZOMBIE_VILLAGER";
			}

			@Override
			public Zombie spawn(final Location location)
			{
				final Zombie zombie = (Zombie) super.spawn(location);
				zombie.setVillager(true);
				return zombie;
			}
		});
	}

	public static void registerNamedEntitySpawner(final NamedEntitySpawner entitySpawner, final String... aliases)
	{
		if (entitySpawner == null)
			throw new IllegalArgumentException("EntitySpawner cannot be null!");
		if (entitySpawner.getName() == null)
			throw new IllegalArgumentException("EntitySpawner's name cannot be null (" + entitySpawner + ")!");
		SPAWNERS.put(entitySpawner.getName().toUpperCase(), entitySpawner);
		for (final String alias : aliases)
			if (alias != null)
				SPAWNERS.put(alias.toUpperCase(), entitySpawner);
	}

	public static NamedEntitySpawner getNamedEntitySpawner(final String name)
	{
		if (name == null)
			return null;
		else
			return SPAWNERS.get(name.toUpperCase());
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

	public NamedEntitySpawnerParamitrisable()
	{
		super(null);
	}

	public NamedEntitySpawnerParamitrisable(final NamedEntitySpawner defaultValue)
	{
		super(defaultValue);
	}

	public NamedEntitySpawnerParamitrisable(final String defaultValue)
	{
		this(SPAWNERS.get(defaultValue.toUpperCase()));
	}

	public NamedEntitySpawnerParamitrisable(final EntityType defaultValue)
	{
		this(defaultValue.name());
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = SPAWNERS.get(parameter.toUpperCase());
		if (value == null)
			throw new CrazyCommandNoSuchException("EntityType", parameter, tabHelp(parameter));
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(parameter);
	}

	public static List<String> tabHelp(String parameter)
	{
		parameter = parameter.toUpperCase();
		final List<String> res = new LinkedList<String>();
		if (parameter.length() == 0)
			res.addAll(SPAWNERS.keySet());
		else
		{
			final List<String> more = new LinkedList<String>();
			for (final Entry<String, NamedEntitySpawner> entry : SPAWNERS.entrySet())
				if (entry.getValue().getName().startsWith(parameter))
					res.add(entry.getKey());
				else if (entry.getValue().getType().name().contains(parameter) || entry.getKey().contains(parameter))
					more.add(entry.getKey());
			res.addAll(more);
		}
		return res.subList(0, Math.min(res.size(), 10));
	}

	private static class DefaultNamedEntitySpawner implements NamedEntitySpawner
	{

		protected final EntityType type;

		public DefaultNamedEntitySpawner(final EntityType type)
		{
			super();
			this.type = type;
		}

		@SuppressWarnings("deprecation")
		@Override
		public String getName()
		{
			if (type.getName() == null)
				return type.name();
			else
				return type.getName().toUpperCase();
		}

		@Override
		public final EntityType getType()
		{
			return type;
		}

		@Override
		public Class<? extends Entity> getEntityClass()
		{
			return type.getEntityClass();
		}

		@Override
		public Entity spawn(final Location location)
		{
			return location.getWorld().spawnEntity(location, type);
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "}";
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			return world.getEntitiesByClass(type.getEntityClass());
		}
	}

	private final static class BabySpawner extends DefaultNamedEntitySpawner
	{

		public BabySpawner(final EntityType type)
		{
			super(type);
		}

		@Override
		public String getName()
		{
			return "BABY_" + super.getName();
		}

		@Override
		public Ageable spawn(final Location location)
		{
			final Ageable ageable = (Ageable) super.spawn(location);
			ageable.setBaby();
			return ageable;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Ageable ageable = (Ageable) it.next();
				if (ageable.isAdult())
					it.remove();
			}
			return entities;
		}
	}

	private final static class HorseColorSpawner extends DefaultNamedEntitySpawner
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
			return color.name() + "_" + super.getName();
		}

		@Override
		public Horse spawn(final Location location)
		{
			final Horse horse = (Horse) super.spawn(location);
			horse.setColor(color);
			return horse;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Horse horse = (Horse) it.next();
				if (horse.getColor() != color)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; color: " + color.name() + "}";
		}
	}

	private final static class HorseVariantSpawner extends DefaultNamedEntitySpawner
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
		public Horse spawn(final Location location)
		{
			final Horse horse = (Horse) super.spawn(location);
			horse.setVariant(variant);
			return horse;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Horse horse = (Horse) it.next();
				if (horse.getVariant() != variant)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; variant: " + variant.name() + "}";
		}
	}

	// Sheep
	private final static class SheepSpawner extends DefaultNamedEntitySpawner
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
		public Sheep spawn(final Location location)
		{
			final Sheep sheep = (Sheep) super.spawn(location);
			sheep.setColor(color);
			return sheep;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Sheep sheep = (Sheep) it.next();
				if (sheep.getColor() != color)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; color: " + color.name() + "}";
		}
	}

	// Skeleton
	private final static class SkeletonSpawner extends DefaultNamedEntitySpawner
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
			return variant.name() + "_" + super.getName();
		}

		@Override
		public Skeleton spawn(final Location location)
		{
			final Skeleton skeleton = (Skeleton) super.spawn(location);
			skeleton.setSkeletonType(variant);
			return skeleton;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Skeleton skeleton = (Skeleton) it.next();
				if (skeleton.getSkeletonType() != variant)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; variant: " + variant.name() + "}";
		}
	}

	// Slime
	private static class SlimeSpawner extends DefaultNamedEntitySpawner
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
			return getSizeText() + "_" + super.getName();
		}

		public final String getSizeText()
		{
			return getSizeText(size);
		}

		@Override
		public Slime spawn(final Location location)
		{
			final Slime slime = (Slime) super.spawn(location);
			slime.setSize(size);
			return slime;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Slime slime = (Slime) it.next();
				if (slime.getSize() != size)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; size: " + size + "}";
		}
	}

	// Villager
	private final static class VillagerSpawner extends DefaultNamedEntitySpawner
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
		public Villager spawn(final Location location)
		{
			final Villager villager = (Villager) super.spawn(location);
			villager.setProfession(variant);
			return villager;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Villager villager = (Villager) it.next();
				if (villager.getProfession() != variant)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; profession: " + variant.name() + "}";
		}
	}
}
