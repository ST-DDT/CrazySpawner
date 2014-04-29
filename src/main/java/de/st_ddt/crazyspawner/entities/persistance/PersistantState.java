package de.st_ddt.crazyspawner.entities.persistance;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;

/**
 * Implementations of this interface must fulfill all requirements as defined in {@link ConfigurationSerializable}.<br>
 * In addition to that it should (not required) be annotated with<br>
 * {@link SerializableAs}("&ltPlugin&gt_Persistence_&ltClassName&gt")
 * 
 * @see ConfigurationSerializable
 */
public interface PersistantState extends ConfigurationSerializable
{

	/**
	 * Reattach this {@link PersistantState} to the given {@link Entity}.
	 * 
	 * @param entity
	 *            The {@link Entity} where this should be attached to.
	 * @param manager
	 *            The {@link PersistanceManager} that watches the {@link PersistantState}s.<br>
	 *            Should be utilized if the state should be kept persistent after the call.
	 */
	public void attachTo(Entity entity, PersistanceManager manager);
}
