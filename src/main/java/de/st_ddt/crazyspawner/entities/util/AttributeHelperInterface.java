package de.st_ddt.crazyspawner.entities.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;

/**
 * Helper interface for {@link AttributeHelper}, providing access to version dependent methods/features.
 */
public interface AttributeHelperInterface
{

	public List<Class<? extends AttributeHelperInterface>> ATTRIBUTEHELPERCLASSES = new ArrayList<>();

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
	public void setAttribute(LivingEntity entity, Attribute attribute, double value);
}
