package de.st_ddt.crazyspawner.entities.util;

/**
 * Generic attributes, that some entities do support.
 */
public enum Attribute
{
	/**
	 * Follow range for hostile mobs.<br>
	 * <b>Default:</b> ~40
	 */
	FOLLOW_RANGE(0, 128),
	/**
	 * Movement speed.<br>
	 * <b>Default:</b> ~0.2 - 0.4<br>
	 * <b>Note:</b> Higher values are possible but look really strange.<br>
	 * Pathfinder won't work properly when using values above 1.
	 */
	MOVEMENT_SPEED(0, 3),
	/**
	 * Knockback resistance chance.<br>
	 * Resisted knockback is still knockback but far less.
	 */
	KNOCKBACK_RESISTANCE(0, 1);

	private double min;
	private double max;

	private Attribute(final double min)
	{
		this(min, Double.MAX_VALUE);
	}

	private Attribute(final double min, final double max)
	{
		this.min = min;
		this.max = max;
	}

	public final double getMin()
	{
		return min;
	}

	public final double getMax()
	{
		return max;
	}

	public double filter(final double value)
	{
		return Math.max(min, Math.min(max, value));
	}

	public String shortName()
	{
		return name().replaceAll("_", "");
	}
}
