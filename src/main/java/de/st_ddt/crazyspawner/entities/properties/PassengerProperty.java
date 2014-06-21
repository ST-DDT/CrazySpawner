package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawnerHelper;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class PassengerProperty extends BasicProperty
{

	protected final NamedEntitySpawner passenger;
	protected final NamedEntitySpawner vehicle;

	public PassengerProperty()
	{
		super();
		this.passenger = null;
		this.vehicle = null;
	}

	public PassengerProperty(final ConfigurationSection config)
	{
		super(config);
		this.passenger = NamedEntitySpawnerHelper.getNamedEntitySpawner(config.getString("passenger", null));
		this.vehicle = NamedEntitySpawnerHelper.getNamedEntitySpawner(config.getString("vehicle", null));
	}

	public PassengerProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final NamedEntitySpawnerParamitrisable passengerParam = (NamedEntitySpawnerParamitrisable) params.get("passenger");
		this.passenger = passengerParam.getValue();
		final NamedEntitySpawnerParamitrisable vehicleParam = (NamedEntitySpawnerParamitrisable) params.get("vehicle");
		this.vehicle = vehicleParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<?> clazz)
	{
		return true;
	}

	@Override
	public void apply(final Entity entity)
	{
		if (passenger != null)
			entity.setPassenger(passenger.spawn(entity.getLocation()));
		if (vehicle != null)
		{
			final Entity mount = vehicle.spawn(entity.getLocation());
			if (mount != null)
				mount.setPassenger(entity);
		}
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final NamedEntitySpawnerParamitrisable passengerParam = new NamedEntitySpawnerParamitrisable(passenger);
		params.put("p", passengerParam);
		params.put("passenger", passengerParam);
		final NamedEntitySpawnerParamitrisable vehicleParam = new NamedEntitySpawnerParamitrisable(vehicle);
		params.put("veh", vehicleParam);
		params.put("vehicle", vehicleParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (passenger == null)
			config.set(path + "passenger", null);
		else
			config.set(path + "passenger", passenger.getName());
		if (vehicle == null)
			config.set(path + "vehicle", null);
		else
			config.set(path + "vehicle", vehicle.getName());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "passenger", "NamedEntitySpawner");
		config.set(path + "vehicle", "NamedEntitySpawner");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.PASSENGER {Passenger}", "CRAZYSPAWNER.ENTITY.PROPERTY.VEHICLE {Vehicle}" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.PASSENGER", target, passenger == null ? "None" : passenger.getName());
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.VEHICLE", target, vehicle == null ? "None" : vehicle.getName());
	}

	@Override
	public boolean equalsDefault()
	{
		return passenger == null && vehicle == null;
	}
}
