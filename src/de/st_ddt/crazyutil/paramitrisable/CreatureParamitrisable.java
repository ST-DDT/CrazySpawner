package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;

import org.bukkit.entity.EntityType;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable.TypedParamitrisable;

public class CreatureParamitrisable extends TypedParamitrisable<EntityType>
{

	protected final static String[] CREATURE_NAMES = getCreatureNames();

	private static String[] getCreatureNames()
	{
		LinkedList<String> res = new LinkedList<String>();
		for (EntityType type : EntityType.values())
			if (type.isAlive())
				res.add(type.toString());
		return res.toArray(new String[0]);
	}

	public CreatureParamitrisable(final EntityType defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		try
		{
			value = EntityType.valueOf(parameter.toUpperCase());
		}
		catch (Exception e)
		{
			throw new CrazyCommandNoSuchException("CreatureType", parameter, CREATURE_NAMES);
		}
		finally
		{
			if (!value.isAlive())
				throw new CrazyCommandParameterException(0, "CreatureType", CREATURE_NAMES);
		}
	}
}
