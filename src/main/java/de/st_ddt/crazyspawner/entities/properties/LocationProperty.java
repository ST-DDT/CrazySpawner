package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class LocationProperty extends BasicProperty
{

	private final boolean relative;
	private final double x, y, z;

	public LocationProperty()
	{
		super();
		this.relative = true;
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public LocationProperty(final ConfigurationSection config)
	{
		super(config);
		this.relative = config.getBoolean("location.relative", true);
		this.x = config.getDouble("location.x", 0);
		this.y = config.getDouble("location.y", 0);
		this.z = config.getDouble("location.z", 0);
	}

	public LocationProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable relativeParam = (BooleanParamitrisable) params.get("relativelocation");
		this.relative = relativeParam.getValue();
		final DoubleParamitrisable xParam = (DoubleParamitrisable) params.get("locationx");
		this.x = xParam.getValue();
		final DoubleParamitrisable yParam = (DoubleParamitrisable) params.get("locationy");
		this.y = yParam.getValue();
		final DoubleParamitrisable zParam = (DoubleParamitrisable) params.get("locationz");
		this.z = zParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<?> clazz)
	{
		return true;
	}

	@Override
	public void apply(final Entity entity)
	{
		final Location location = entity.getLocation();
		if (relative)
			location.add(x, y, z);
		else
		{
			location.setX(x);
			location.setY(y);
			location.setZ(z);
		}
		entity.teleport(location, TeleportCause.PLUGIN);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable relativeParam = new BooleanParamitrisable(relative);
		params.put("relloc", relativeParam);
		params.put("relativeloc", relativeParam);
		params.put("rellocation", relativeParam);
		params.put("relativelocation", relativeParam);
		params.put("locrel", relativeParam);
		params.put("locrelative", relativeParam);
		params.put("locationrel", relativeParam);
		params.put("locationrelative", relativeParam);
		final DoubleParamitrisable xParam = new DoubleParamitrisable(x);
		params.put("x", xParam);
		params.put("locx", xParam);
		params.put("locationx", xParam);
		final DoubleParamitrisable yParam = new DoubleParamitrisable(y);
		params.put("y", yParam);
		params.put("locy", yParam);
		params.put("locationy", yParam);
		final DoubleParamitrisable zParam = new DoubleParamitrisable(z);
		params.put("z", zParam);
		params.put("locz", zParam);
		params.put("locationz", zParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "location.relative", relative);
		config.set(path + "location.x", x);
		config.set(path + "location.y", y);
		config.set(path + "location.z", z);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "location.relative", "boolean (true/false)");
		config.set(path + "location.x", "double (0.0 = default)");
		config.set(path + "location.y", "double (0.0 = default)");
		config.set(path + "location.z", "double (0.0 = default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.LOCATION {Relative} {X} {Y} {Z}")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.LOCATION", target, relative, x, y, z);
	}

	@Override
	public boolean equalsDefault()
	{
		return relative && x == 0 && y == 0 && z == 0;
	}
}
