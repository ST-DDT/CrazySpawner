package de.st_ddt.crazyutil.entities;

public interface ParentedEntitySpawner extends EntitySpawner, ApplyableEntitySpawner
{

	public EntitySpawner getParentSpawner();
}
