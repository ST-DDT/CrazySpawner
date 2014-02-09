package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.EntityType;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;

public class NamedEntitySpawnerParamitrisable extends TypedParamitrisable<NamedEntitySpawner>
{

	protected final static Map<String, NamedEntitySpawner> NAMEDENTITYSPAWNERS = NamedEntitySpawner.NAMEDENTITYSPAWNERS;

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
		this(NAMEDENTITYSPAWNERS.get(defaultValue.toUpperCase()));
	}

	public NamedEntitySpawnerParamitrisable(final EntityType defaultValue)
	{
		this(defaultValue.name());
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = NAMEDENTITYSPAWNERS.get(parameter.toUpperCase());
		if (value == null)
			throw new CrazyCommandNoSuchException("EntityType", parameter, tabHelp(parameter));
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(parameter);
	}

	public static List<String> tabHelp(final String parameter)
	{
		return tabHelp(NAMEDENTITYSPAWNERS, parameter);
	}

	public static List<String> tabHelp(final Map<String, ? extends NamedEntitySpawner> spawners, String parameter)
	{
		parameter = parameter.toUpperCase();
		final List<String> res = new LinkedList<String>();
		if (parameter.length() == 0)
			res.addAll(spawners.keySet());
		else
		{
			final List<String> more = new LinkedList<String>();
			for (final Entry<String, ? extends NamedEntitySpawner> entry : spawners.entrySet())
				if (entry.getValue().getName().toUpperCase().startsWith(parameter))
					res.add(entry.getKey());
				else if (typedSpawnerName(entry.getValue()).startsWith(parameter))
					res.add(typedSpawnerName(entry.getValue()));
				else if (entry.getKey().contains(parameter))
					more.add(entry.getKey());
			res.addAll(more);
		}
		return res.subList(0, Math.min(res.size(), 10));
	}

	private static String typedSpawnerName(final NamedEntitySpawner spawner)
	{
		return typedName(spawner.getEntityType(), spawner.getName());
	}

	private static String typedName(final EntityType type, final String name)
	{
		return EntitySpawnerHelper.getEntityTypeName(type) + ":" + name;
	}
}
