package de.st_ddt.crazyutil.entities;

/**
 * A list of possible EntitySpawner types.
 */
public enum EntitySpawnerType
{
	/**
	 * Raw Spawner, very basic none configurable spawner, cannot have parents.
	 */
	RAW,
	/**
	 * Special Spawner, spawner is {@link ConfigurableEntitySpawner}, cannot have parents, cannot be replaced by a {@link #CONFIGURABLE}
	 */
	SPECIAL,
	/**
	 * Configurable Spawner, spawner is {@link ConfigurableEntitySpawner}.
	 */
	CONFIGURABLE,
	/**
	 * Named Spawner, spawner is {@link NamedEntitySpawner}.
	 */
	NAMED,
	/**
	 * Random Spawner, spawns a Random Entity (based on setup).
	 */
	RANDOM,
}
