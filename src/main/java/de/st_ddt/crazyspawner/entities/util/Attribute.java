package de.st_ddt.crazyspawner.entities.util;

/**
 * Generic attributes, that some entities do support.
 */
public enum Attribute
{
	FOLLOW_RANGE(0),
	MOVEMENT_SPEED(0),
	KNOCKBACK_RESISTANCE(0);

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
