package de.st_ddt.crazyspawner.entities.util;

import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyspawner.CrazySpawner;

public class AttributeHelper
{

	private AttributeHelper()
	{
	}

	private static AttributeHelperInterface attributeHelper;

	/**
	 * This method initializes this ActionHelper with a matching implementation of the required interface.<br>
	 * This method will be called by {@link CrazySpawner#initialize()}.
	 * 
	 * @return True, if the AttributeHelper has been initialized properly, false otherwise.
	 */
	public static boolean initialize()
	{
		for (final Class<? extends AttributeHelperInterface> clazz : AttributeHelperInterface.ATTRIBUTEHELPERCLASSES)
			try
			{
				attributeHelper = clazz.newInstance();
				return true;
			}
			catch (final Throwable t)
			{
				t.printStackTrace();
			}
		return false;
	}

	/**
	 * Tries to set the attribute to the client.
	 * 
	 * @param entity
	 *            The {@link LivingEntity} which {@link Attribute} should be set.
	 * @param attribute
	 *            The {@link Attribute} that should be set.
	 * @param value
	 *            The new value for the attribute.
	 */
	public static void setAttribute(final LivingEntity entity, final Attribute attribute, final double value)
	{
		attributeHelper.setAttribute(entity, attribute, value);
	}
}
