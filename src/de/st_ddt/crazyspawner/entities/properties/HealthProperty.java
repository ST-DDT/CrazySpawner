package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CompatibilityHelper;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class HealthProperty extends BasicProperty
{

	protected final double minHealth;
	protected final double maxHealth;

	public HealthProperty()
	{
		super();
		this.minHealth = -1;
		this.maxHealth = -1;
	}

	public HealthProperty(final ConfigurationSection config)
	{
		super(config);
		this.minHealth = getSecureValue(config.getDouble("minHealth", config.getDouble("maxHealth", -1)));
		this.maxHealth = getSecureValue(config.getDouble("maxHealth", -1));
	}

	public HealthProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable minHealthParam = (DoubleParamitrisable) params.get("minhealth");
		final DoubleParamitrisable maxHealthParam = (DoubleParamitrisable) params.get("maxhealth");
		this.minHealth = getSecureValue(Math.min(minHealthParam.getValue(), maxHealthParam.getValue()));
		this.maxHealth = getSecureValue(Math.min(minHealthParam.getValue(), maxHealthParam.getValue()));
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return LivingEntity.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		if (minHealth == -1)
			return;
		final LivingEntity living = (LivingEntity) entity;
		final double health = getRandom(minHealth, maxHealth);
		CompatibilityHelper.setMaxHealth(living, health);
		CompatibilityHelper.setHealth(living, health);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable minHealth = new DoubleParamitrisable(this.minHealth);
		params.put("minh", minHealth);
		params.put("minHealth", minHealth);
		final DoubleParamitrisable maxHealth = new DoubleParamitrisable(this.maxHealth);
		params.put("maxh", maxHealth);
		params.put("maxHealth", maxHealth);
		final DoubleParamitrisable health = new DoubleParamitrisable(null)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				minHealth.setValue(getValue());
				maxHealth.setValue(getValue());
			}
		};
		params.put("h", health);
		params.put("health", health);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "minHealth", minHealth);
		config.set(path + "maxHealth", maxHealth);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "minHealth", "double (-1 = default)");
		config.set(path + "maxHealth", "double (-1 = default)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.HEALTH.DEFAULT", "CRAZYSPAWNER.ENTITY.PROPERTY.HEALTH $MinHealth$ $MaxHealth$" })
	public void show(final CommandSender target)
	{
		if (minHealth == -1)
			CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.HEALTH.DEFAULT", target);
		else
			CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.HEALTH", target, minHealth, maxHealth);
	}

	@Override
	public boolean equalsDefault()
	{
		return minHealth == -1;
	}
}
