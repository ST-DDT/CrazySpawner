package de.st_ddt.crazyutil;

import java.util.regex.Pattern;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class VectorUtil {

	private final static double ANGLECONVERTER = 180 / Math.PI;
	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");

	/**
	 * Converts the given BlockFace to Cartesian coordinates.
	 *
	 * @param direction
	 *            The direction of the {@link BlockFace}.
	 * @return The unit vector representing the given {@link BlockFace} direction as Cartesian coordinates.
	 */
	public static Vector getVector(final BlockFace direction) {
		return new Vector(direction.getModX(), direction.getModY(), direction.getModZ()).normalize();
	}

	/**
	 * Converts the given Spherical coordinates to Cartesian coordinates.
	 *
	 * @param radius
	 *            The radius (will result in the vector's length / velocity).
	 * @param yaw
	 *            The yaw rotation in radians.
	 * @param pitch
	 *            The pitch rotation in radians.
	 * @return The vector representing the given Spherical coordinates as Cartesian coordinates.
	 */
	public static Vector getVector(final double radius, final double yaw, final double pitch) {
		final double cosPitch = Math.cos(pitch);
		final double x = -Math.sin(yaw) * cosPitch * radius;
		final double z = Math.cos(yaw) * cosPitch * radius;
		final double y = Math.sin(pitch) * radius;
		return new Vector(x, y, z);
	}

	/**
	 * Converts the given Spherical coordinates to Cartesian coordinates.
	 *
	 * @param radius
	 *            The radius (will result in the vector's length / velocity).
	 * @param yaw
	 *            The yaw rotation in degree.
	 * @param pitch
	 *            The pitch rotation in degree.
	 * @return The vector representing the given Spherical coordinates as Cartesian coordinates
	 */
	public static Vector getVectorDeg(final double radius, final double yaw, final double pitch) {
		return getVector(radius, degToRad(yaw), degToRad(pitch));
	}

	/**
	 * Converts the given Cartesian coordinates to Spherical coordinates.
	 *
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 * @param z
	 *            The z coordinates.
	 * @return An array consisting of length, yaw (in radians), pitch (in radians).
	 */
	public static double[] fromXYZ(final double x, final double y, final double z) {
		final double[] res = new double[3];
		res[0] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
		res[1] = -Math.atan2(x, z);
		res[2] = Math.asin(y / res[0]);
		return res;
	}

	/**
	 * Converts the given Cartesian coordinates to Spherical coordinates.
	 *
	 * @param x
	 *            The x coordinates.
	 * @param y
	 *            The y coordinates.
	 * @param z
	 *            The z coordinates.
	 * @return An array consisting of length, yaw (in degree), pitch (in degree).
	 */
	public static double[] fromXYZDeg(final double x, final double y, final double z) {
		final double[] res = fromXYZ(x, y, z);
		res[1] = radToDeg(res[1]);
		res[2] = radToDeg(res[2]);
		return res;
	}

	/**
	 * Converts the given Cartesian coordinates to Spherical coordinates.
	 *
	 * @param vector
	 *            The vector to be converted.
	 * @return An array consisting of length, yaw (in radians), pitch (in radians).
	 */
	public static double[] fromVector(final Vector vector) {
		return fromXYZ(vector.getX(), vector.getY(), vector.getZ());
	}

	/**
	 * Converts the given Cartesian coordinates to Spherical coordinates.
	 *
	 * @param vector
	 *            The vector to be converted.
	 * @return An array consisting of length, yaw (in degree), pitch (in degree).
	 */
	public static double[] fromVectorDeg(final Vector vector) {
		return fromXYZDeg(vector.getX(), vector.getY(), vector.getZ());
	}

	public static double radToDeg(final double value) {
		return value * ANGLECONVERTER;
	}

	public static double degToRad(final double value) {
		return value / ANGLECONVERTER;
	}

	public static Vector rotate(final Vector vector, final double yaw, final double pitch) {
		final double[] cart = fromVector(vector);
		cart[1] += yaw;
		cart[2] += pitch;
		return getVector(cart[0], cart[1], cart[2]);
	}
}
