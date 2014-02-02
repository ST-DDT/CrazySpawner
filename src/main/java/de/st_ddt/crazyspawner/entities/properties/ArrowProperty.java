package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class ArrowProperty extends BasicProperty
{

	protected final int knockback;
	protected final Boolean critical;

	public ArrowProperty()
	{
		super();
		this.knockback = -1;
		this.critical = null;
	}

	public ArrowProperty(final ConfigurationSection config)
	{
		super(config);
		this.knockback = getSecureValue(config.getInt("knockback", -1));
		if (config.getBoolean("critical", false))
			this.critical = false;
		else if (!config.getBoolean("critical", true))
			this.critical = false;
		else
			this.critical = null;
	}

	public ArrowProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final IntegerParamitrisable knockbackParam = (IntegerParamitrisable) params.get("knockback");
		this.knockback = knockbackParam.getValue();
		final BooleanParamitrisable criticalParam = (BooleanParamitrisable) params.get("critical");
		this.critical = criticalParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return Arrow.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Arrow arrow = (Arrow) entity;
		if (knockback != -1)
			arrow.setKnockbackStrength(knockback);
		if (critical != null)
			arrow.setCritical(critical);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final IntegerParamitrisable knockbackParam = new IntegerParamitrisable(-1);
		params.put("knockback", knockbackParam);
		final BooleanParamitrisable criticalParam = new BooleanParamitrisable(critical);
		params.put("critical", criticalParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "knockback", knockback);
		config.set(path + "critical", critical);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "knockback", "int (-1 = default)");
		config.set(path + "critical", "Boolean (true/false/default)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.ARROW.KNOCKBACK {Knockback}", "CRAZYSPAWNER.ENTITY.PROPERTY.ARROW.CRITICAL {Critical}" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ARROW.KNOCKBACK", target, knockback == -1 ? "Default" : knockback);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ARROW.CRITICAL", target, critical == null ? "Default" : critical);
	}

	@Override
	public boolean equalsDefault()
	{
		return knockback == -1 && critical == null;
	}
}
