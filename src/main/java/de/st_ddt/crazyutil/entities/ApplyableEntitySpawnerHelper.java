package de.st_ddt.crazyutil.entities;

import de.st_ddt.crazyspawner.entities.spawners.NamedApplyableSpawner;

public class ApplyableEntitySpawnerHelper
{

	public static boolean isApplicable(final EntitySpawner spawner)
	{
		if (spawner instanceof ApplyableEntitySpawner)
			return true;
		else if (spawner instanceof ParentedEntitySpawner && ((ParentedEntitySpawner) spawner).getParentSpawner() instanceof ApplyableEntitySpawner)
			return true;
		else
			return false;
	}

	public static ApplyableEntitySpawner wrapSpawner(final EntitySpawner spawner)
	{
		if (spawner instanceof ApplyableEntitySpawner)
			return (ApplyableEntitySpawner) spawner;
		else if (spawner instanceof ParentedEntitySpawner)
		{
			final EntitySpawner parent = ((ParentedEntitySpawner) spawner).getParentSpawner();
			if (parent instanceof ApplyableEntitySpawner)
				if (spawner instanceof NamedEntitySpawner)
					return new NamedApplyableSpawner((ApplyableEntitySpawner) parent, ((NamedEntitySpawner) spawner).getName());
				else
					return (ApplyableEntitySpawner) parent;
			else
				return null;
		}
		else
			return null;
	}
}
