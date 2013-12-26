package de.st_ddt.crazyutil.entities;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityMatcherHelper
{

	public static Collection<? extends Entity> getMatchingEntites(final World world, final EntityType type, final EntityMatcher... matchers)
	{
		final Collection<? extends Entity> entities = world.getEntitiesByClass(type.getEntityClass());
		for (final EntityMatcher matcher : matchers)
			filterMatchingEntites(entities, matcher);
		return entities;
	}

	public static <T extends Entity> Collection<T> getMatchingEntites(final World world, final Class<T> clazz, final EntityMatcher... matchers)
	{
		final Collection<T> entities = world.getEntitiesByClass(clazz);
		for (final EntityMatcher matcher : matchers)
			filterMatchingEntites(entities, matcher);
		return entities;
	}

	public static Collection<Entity> getMatchingEntites(final World world, final Class<?>[] clazzes, final EntityMatcher... matchers)
	{
		final Collection<Entity> entities = world.getEntitiesByClasses(clazzes);
		for (final EntityMatcher matcher : matchers)
			filterMatchingEntites(entities, matcher);
		return entities;
	}

	public static Collection<? extends Entity> getMatchingEntites(final Location location, final double distance, final EntityType type, final EntityMatcher... matchers)
	{
		final Collection<? extends Entity> entities = getMatchingEntites(location.getWorld(), type, matchers);
		filterMatchingEntites(entities, new DistanceMatcher(location, distance));
		return entities;
	}

	public static <T extends Entity> Collection<T> getMatchingEntites(final Location location, final double distance, final Class<T> clazz, final EntityMatcher... matchers)
	{
		final Collection<T> entities = getMatchingEntites(location.getWorld(), clazz, matchers);
		filterMatchingEntites(entities, new DistanceMatcher(location, distance));
		return entities;
	}

	public static Collection<Entity> getMatchingEntites(final Location location, final double distance, final Class<?>[] clazzes, final EntityMatcher... matchers)
	{
		final Collection<Entity> entities = getMatchingEntites(location.getWorld(), clazzes, matchers);
		filterMatchingEntites(entities, new DistanceMatcher(location, distance));
		return entities;
	}

	/**
	 * Removes all {@link Entity}s from the given collection, which do not match to the given {@link EntityMatcher}.
	 * 
	 * @param entities
	 *            The {@link Collection} that should be filtered.
	 * @param matcher
	 *            The {@link EntityMatcher} used to filter the {@link Entity}s;
	 */
	public static void filterMatchingEntites(final Iterable<? extends Entity> entities, final EntityMatcher matcher)
	{
		final Iterator<? extends Entity> it = entities.iterator();
		while (it.hasNext())
			if (!matcher.matches(it.next()))
				it.remove();
	}

	protected EntityMatcherHelper()
	{
	}

	public final static class DistanceMatcher implements EntityMatcher
	{

		private final Location center;
		private final double distance;

		public DistanceMatcher(final Location center, final double distance)
		{
			super();
			this.center = center;
			this.distance = distance;
		}

		@Override
		public boolean matches(final Entity entity)
		{
			final Location location = entity.getLocation();
			if (location.getWorld() == center.getWorld())
				return center.distance(location) <= distance;
			else
				return false;
		}
	}

	public final static class NotMatcher implements EntityMatcher
	{

		private final EntityMatcher matcher;

		public NotMatcher(final EntityMatcher matcher)
		{
			super();
			this.matcher = matcher;
		}

		@Override
		public boolean matches(final Entity entity)
		{
			return !matcher.matches(entity);
		}
	}
}
