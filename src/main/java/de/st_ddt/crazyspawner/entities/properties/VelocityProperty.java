package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.VectorParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class VelocityProperty extends BasicProperty
{

	protected final double velocityMin;
	protected final double velocityMax;
	/**
	 * The yaw angle in degree.
	 */
	protected final int yaw;
	/**
	 * The yaw angle-offset in degree.
	 */
	protected final int yawOff;
	/**
	 * The pitch angle in degree.
	 */
	protected final int pitch;
	/**
	 * The pitch angle-offset in degree.
	 */
	protected final int pitchOff;

	public VelocityProperty()
	{
		super();
		this.velocityMin = 0;
		this.velocityMax = 0;
		this.yaw = 0;
		this.yawOff = 0;
		this.pitch = 0;
		this.pitchOff = 0;
	}

	public VelocityProperty(final Vector vector)
	{
		super();
		final double[] velocity = VectorParamitrisable.fromVectorDeg(vector);
		this.velocityMin = velocity[0];
		this.velocityMax = velocity[0];
		this.yaw = (int) velocity[1];
		this.yawOff = 0;
		this.pitch = (int) velocity[2];
		this.pitchOff = 0;
	}

	public VelocityProperty(final ConfigurationSection config)
	{
		super(config);
		if (config.contains("velocity.pitch"))
		{
			this.velocityMin = config.getDouble("velocity.speedMin", 0);
			this.velocityMax = config.getDouble("velocity.speedMax", 0);
			this.yaw = config.getInt("velocity.yaw", 0);
			this.yawOff = config.getInt("velocity.yawOff", 0);
			this.pitch = config.getInt("velocity.pitch", 0);
			this.pitchOff = config.getInt("velocity.pitchOff", 0);
		}
		else
		{
			final double x = config.getDouble("velocity.X", 0);
			final double y = config.getDouble("velocity.Y", 0);
			final double z = config.getDouble("velocity.Z", 0);
			final double[] velocity = VectorParamitrisable.fromXYZDeg(x, y, z);
			this.velocityMin = velocity[0];
			this.velocityMax = velocity[0];
			this.yaw = (int) velocity[1];
			this.yawOff = 0;
			this.pitch = (int) velocity[2];
			this.pitchOff = 0;
		}
	}

	public VelocityProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		// Speed
		final DoubleParamitrisable velocityMinParam = (DoubleParamitrisable) params.get("velocitymin");
		this.velocityMin = velocityMinParam.getValue();
		final DoubleParamitrisable velocityMaxParam = (DoubleParamitrisable) params.get("velocitymax");
		this.velocityMax = velocityMaxParam.getValue();
		// Yaw
		final IntegerParamitrisable yawMinParam = (IntegerParamitrisable) params.get("velocityyaw");
		this.yaw = yawMinParam.getValue();
		final IntegerParamitrisable yawMaxParam = (IntegerParamitrisable) params.get("velocityyawoff");
		this.yawOff = yawMaxParam.getValue();
		// Pitch
		final IntegerParamitrisable pitchMinParam = (IntegerParamitrisable) params.get("velocitypitch");
		this.pitch = pitchMinParam.getValue();
		final IntegerParamitrisable pitchMaxParam = (IntegerParamitrisable) params.get("velocitypitchoff");
		this.pitchOff = pitchMaxParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<?> clazz)
	{
		return !Fireball.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final double velocity = getRandom(velocityMin, velocityMax);
		final double yaw = getOff(this.yaw, yawOff);
		final double pitch = getOff(this.pitch, pitchOff);
		entity.setVelocity(VectorParamitrisable.getVectorDeg(velocity, yaw, pitch));
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		// Speed
		final DoubleParamitrisable velocityMinParam = new DoubleParamitrisable(velocityMin);
		params.put("vmin", velocityMinParam);
		params.put("velomin", velocityMinParam);
		params.put("velocitymin", velocityMinParam);
		params.put("speedmin", velocityMinParam);
		final DoubleParamitrisable velocityMaxParam = new DoubleParamitrisable(velocityMax);
		params.put("vmax", velocityMaxParam);
		params.put("velomax", velocityMaxParam);
		params.put("velocitymax", velocityMaxParam);
		params.put("speedmax", velocityMaxParam);
		// Yaw
		final IntegerParamitrisable yawParam = new IntegerParamitrisable(yaw);
		params.put("vyaw", yawParam);
		params.put("veloyaw", yawParam);
		params.put("velocityyaw", yawParam);
		final IntegerParamitrisable yawOffParam = new IntegerParamitrisable(yawOff);
		params.put("vyo", yawOffParam);
		params.put("vyawoff", yawOffParam);
		params.put("veloyawoff", yawOffParam);
		params.put("velocityyawoff", yawOffParam);
		// Pitch
		final IntegerParamitrisable pitchParam = new IntegerParamitrisable(pitch);
		params.put("vp", pitchParam);
		params.put("vpitch", pitchParam);
		params.put("velopitch", pitchParam);
		params.put("velocitypitch", pitchParam);
		final IntegerParamitrisable pitchOffParam = new IntegerParamitrisable(pitchOff);
		params.put("vpo", pitchOffParam);
		params.put("vpitchoff", pitchOffParam);
		params.put("velopitchoff", pitchOffParam);
		params.put("velocitypitchoff", pitchOffParam);
		// X pure
		final DoubleParamitrisable velocityXParam = new DoubleParamitrisable(null)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				final double vx = getValue();
				final Vector vector = VectorParamitrisable.getVectorDeg((velocityMin + velocityMax) / 2, yaw, pitch);
				vector.setX(vx);
				final double[] res = VectorParamitrisable.fromVectorDeg(vector);
				velocityMinParam.setValue(res[0]);
				velocityMaxParam.setValue(res[0]);
				yawParam.setValue((int) res[1]);
				yawOffParam.setValue(0);
				pitchParam.setValue((int) res[2]);
				pitchOffParam.setValue(0);
			}
		};
		params.put("vx", velocityXParam);
		params.put("velx", velocityXParam);
		params.put("velocityx", velocityXParam);
		// Y pure
		final DoubleParamitrisable velocityYParam = new DoubleParamitrisable(null)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				final double vy = getValue();
				final Vector vector = VectorParamitrisable.getVectorDeg((velocityMin + velocityMax) / 2, yaw, pitch);
				vector.setY(vy);
				final double[] res = VectorParamitrisable.fromVectorDeg(vector);
				velocityMinParam.setValue(res[0]);
				velocityMaxParam.setValue(res[0]);
				yawParam.setValue((int) res[1]);
				yawOffParam.setValue(0);
				pitchParam.setValue((int) res[2]);
				pitchOffParam.setValue(0);
			}
		};
		params.put("vy", velocityYParam);
		params.put("vely", velocityYParam);
		params.put("velocityy", velocityYParam);
		// Z pure
		final DoubleParamitrisable velocityZParam = new DoubleParamitrisable(null)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				final double vz = getValue();
				final Vector vector = VectorParamitrisable.getVectorDeg((velocityMin + velocityMax) / 2, yaw, pitch);
				vector.setZ(vz);
				final double[] res = VectorParamitrisable.fromVectorDeg(vector);
				velocityMinParam.setValue(res[0]);
				velocityMaxParam.setValue(res[0]);
				yawParam.setValue((int) res[1]);
				yawOffParam.setValue(0);
				pitchParam.setValue((int) res[2]);
				pitchOffParam.setValue(0);
			}
		};
		params.put("vz", velocityZParam);
		params.put("velz", velocityZParam);
		params.put("velocityz", velocityZParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "velocity.speedMin", velocityMin);
		config.set(path + "velocity.speedMax", velocityMax);
		config.set(path + "velocity.yaw", yaw);
		config.set(path + "velocity.yawOff", yawOff);
		config.set(path + "velocity.pitch", pitch);
		config.set(path + "velocity.pitchOff", pitchOff);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "velocity.speedMin", "double");
		config.set(path + "velocity.speedMax", "double");
		config.set(path + "velocity.yaw", "int (0-360)");
		config.set(path + "velocity.yawOff", "int (0-360)");
		config.set(path + "velocity.pitch", "int (0-360)");
		config.set(path + "velocity.pitchOff", "int (0-360)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.VELOCITY {SpeedMin} {SpeedMax} {Yaw} {YawOff} {Pitch} {PitchOff}")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.VELOCITY", target, velocityMin, velocityMax, yaw, yawOff, pitch, pitchOff);
	}

	@Override
	public boolean equalsDefault()
	{
		return velocityMin == 0 && velocityMax == 0 && yaw == 0 && yawOff == 0 && pitch == 0 && pitchOff == 0;
	}

	protected double getOff(final double center, final double off)
	{
		if (off <= 0)
			return center;
		if (RANDOM.nextBoolean())
			return center + RANDOM.nextDouble() * off;
		else
			return center - RANDOM.nextDouble() * off;
	}
}
