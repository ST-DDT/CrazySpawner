package de.st_ddt.crazyspawner.craftbukkit.v1_7_R4.entities.util;

import net.minecraft.server.v1_7_R4.AttributeInstance;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import net.minecraft.server.v1_7_R4.IAttribute;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyspawner.entities.util.Attribute;
import de.st_ddt.crazyspawner.entities.util.AttributeHelperInterface;

public class AttributeHelperImpl implements AttributeHelperInterface
{

	public static EntityLiving getHandle(final LivingEntity entity)
	{
		return ((CraftLivingEntity) entity).getHandle();
	}

	@Override
	public void setAttribute(final LivingEntity entity, final Attribute attribute, final double value)
	{
		final IAttribute attr = resolveAttribute(attribute);
		if (attr != null)
		{
			final AttributeInstance attrInstace = getHandle(entity).getAttributeInstance(attr);
			if (attrInstace != null)
				attrInstace.setValue(value);
		}
	}

	private IAttribute resolveAttribute(final Attribute attribute)
	{
		switch (attribute)
		{
			case FOLLOW_RANGE:
				return GenericAttributes.b;
			case KNOCKBACK_RESISTANCE:
				return GenericAttributes.c;
			case MOVEMENT_SPEED:
				return GenericAttributes.d;
			default:
				return null;
		}
	}
}
