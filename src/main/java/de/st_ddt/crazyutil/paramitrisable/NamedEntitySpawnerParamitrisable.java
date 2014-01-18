package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.EntityType;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
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

	public static List<String> tabHelp(String parameter)
	{
		parameter = parameter.toUpperCase();
		final List<String> res = new LinkedList<String>();
		if (parameter.length() == 0)
			res.addAll(NAMEDENTITYSPAWNERS.keySet());
		else
		{
			final List<String> more = new LinkedList<String>();
			for (final Entry<String, NamedEntitySpawner> entry : NAMEDENTITYSPAWNERS.entrySet())
				if (entry.getValue().getName().startsWith(parameter))
					res.add(entry.getKey());
				else if (entry.getValue().getType().name().contains(parameter) || entry.getKey().contains(parameter))
					more.add(entry.getKey());
			res.addAll(more);
		}
		return res.subList(0, Math.min(res.size(), 10));
	}
}
