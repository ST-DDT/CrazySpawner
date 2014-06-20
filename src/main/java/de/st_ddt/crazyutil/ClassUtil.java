package de.st_ddt.crazyutil;

import java.util.HashSet;
import java.util.Set;

public class ClassUtil
{

	@SafeVarargs
	public static Set<Class<?>> getSharedClasses(final Set<Class<?>>... classes)
	{
		final Set<Class<?>> shared = new HashSet<Class<?>>();
		if (classes.length == 0)
			return shared;
		shared.addAll(classes[0]);
		for (int i = 1; i < classes.length; i++)
			shared.retainAll(classes[i]);
		return shared;
	}

	public static Set<Class<?>> getSharedParentClasses(final Class<?>... classes)
	{
		@SuppressWarnings("unchecked")
		final Set<Class<?>>[] classesClasses = new Set[classes.length];
		for (int i = 0; i < classes.length; i++)
			classesClasses[i] = getClassSet(classes[i]);
		return getSharedClasses(classesClasses);
	}

	public static Set<Class<?>> getSharedClasses(final Object... objects)
	{
		final Set<Class<?>> shared = new HashSet<Class<?>>();
		if (objects.length == 0)
			return shared;
		shared.addAll(getClassSet(objects[0].getClass()));
		for (int i = 1; i < objects.length; i++)
			shared.retainAll(getClassSet(objects[i].getClass()));
		return shared;
	}

	private static Set<Class<?>> getClassSet(final Class<?> clazz)
	{
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		for (final Class<?> cls : clazz.getClasses())
			classes.add(cls);
		return classes;
	}
}
