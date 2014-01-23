package de.st_ddt.crazyspawner.entities;

import org.bukkit.entity.Entity;

import de.st_ddt.crazyutil.entities.ApplyableEntitySpawner;
import de.st_ddt.crazyutil.entities.EntitySpawnerType;

public class NamedApplyableSpawner extends BasicParentedSpawner implements ApplyableEntitySpawner
{

	public NamedApplyableSpawner(final ApplyableEntitySpawner spawner)
	{
		super(spawner);
	}

	@Override
	public EntitySpawnerType getSpawnerType()
	{
		return EntitySpawnerType.NAMED;
	}

	@Override
	public void apply(final Entity entity)
	{
		((ApplyableEntitySpawner) spawner).apply(entity);
	}
}
