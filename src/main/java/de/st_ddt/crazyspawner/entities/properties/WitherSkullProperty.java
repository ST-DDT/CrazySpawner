package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.WitherSkull;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class WitherSkullProperty extends BasicProperty
{

	protected final Boolean charged;

	public WitherSkullProperty()
	{
		super();
		this.charged = null;
	}

	public WitherSkullProperty(final ConfigurationSection config)
	{
		super(config);
		if (config.getBoolean("charged", false))
			this.charged = false;
		else if (!config.getBoolean("charged", true))
			this.charged = false;
		else
			this.charged = null;
	}

	public WitherSkullProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable chargedParam = (BooleanParamitrisable) params.get("charged");
		this.charged = chargedParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<?> clazz)
	{
		return WitherSkull.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final WitherSkull witherSkull = (WitherSkull) entity;
		if (charged != null)
			witherSkull.setCharged(charged);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable chargedParam = new BooleanParamitrisable(charged);
		params.put("powered", chargedParam);
		params.put("charged", chargedParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "charged", charged);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "charged", "Boolean (true/false/default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.WITHERSKULL.CHARGED {Charged}")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.WITHERSKULL.CHARGED", target, charged == null ? "Default" : charged);
	}

	@Override
	public boolean equalsDefault()
	{
		return charged == null;
	}
}
