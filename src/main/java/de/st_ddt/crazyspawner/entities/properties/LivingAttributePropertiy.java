package de.st_ddt.crazyspawner.entities.properties;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.persistance.PersistanceManager;
import de.st_ddt.crazyspawner.entities.persistance.PersistantState;
import de.st_ddt.crazyspawner.entities.util.Attribute;
import de.st_ddt.crazyspawner.entities.util.AttributeHelper;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

@SerializableAs("CrazySpawner_Persistence_LivingAttributePropertiy")
public class LivingAttributePropertiy extends BasicProperty implements PersistantState
{

	public static final String PERSISTANCEHEADER = "LivingAttributeMeta";
	static
	{
		PersistanceManager.registerPersistableState(LivingAttributePropertiy.class);
	}
	private final Map<Attribute, Double> attributes = new EnumMap<Attribute, Double>(Attribute.class);

	public LivingAttributePropertiy()
	{
		super();
	}

	public LivingAttributePropertiy(final ConfigurationSection config)
	{
		super(config);
		final ConfigurationSection attributesConfig = config.getConfigurationSection("attributes");
		if (attributesConfig != null)
			for (final Attribute attribute : Attribute.values())
				if (!attributesConfig.getString(attribute.name(), "default").equalsIgnoreCase("default"))
					setAttribute(attribute, attributesConfig.getDouble(attribute.name(), 1));
	}

	private LivingAttributePropertiy(final Map<String, Object> params, final boolean foo)
	{
		super();
		for (final Attribute attribute : Attribute.values())
		{
			final Double value = (Double) params.get(attribute.name());
			if (value != null)
				setAttribute(attribute, value);
		}
	}

	public LivingAttributePropertiy(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		for (final Attribute attribute : Attribute.values())
		{
			final DoubleParamitrisable attrParam = (DoubleParamitrisable) params.get("attribute" + attribute.name().toLowerCase());
			final Double value = attrParam.getValue();
			if (value != null)
				setAttribute(attribute, value);
		}
	}

	public void setAttribute(final Attribute attribute, final double value)
	{
		this.attributes.put(attribute, attribute.filter(value));
	}

	public void removeAttribute(final Attribute attribute)
	{
		this.attributes.remove(attribute);
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return LivingEntity.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final LivingEntity living = (LivingEntity) entity;
		for (final Entry<Attribute, Double> entry : attributes.entrySet())
			AttributeHelper.setAttribute(living, entry.getKey(), entry.getValue().doubleValue());
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		for (final Attribute attribute : Attribute.values())
		{
			final DoubleParamitrisable attrValue = new DoubleParamitrisable(attributes.get(attribute));
			params.put(attribute.shortName().toLowerCase(), attrValue);
			params.put(attribute.name().toLowerCase(), attrValue);
			params.put("attribute" + attribute.shortName().toLowerCase(), attrValue);
			params.put("attribute" + attribute.name().toLowerCase(), attrValue);
		}
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "attributes", null);
		for (final Attribute attribute : Attribute.values())
		{
			final Double value = attributes.get(attribute);
			if (value == null)
				config.set(path + "attributes." + attribute.name(), "default");
			else
				config.set(path + "attributes." + attribute.name(), value);
		}
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "attributes", null);
		for (final Attribute attribute : Attribute.values())
			if (attribute.getMax() == Double.MAX_VALUE)
				config.set(path + "attributes." + attribute.name(), "double (" + attribute.getMin() + ".../ default)");
			else
				config.set(path + "attributes." + attribute.name(), "double (" + attribute.getMin() + "..." + attribute.getMax() + "/ default)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.ATTRIBUTE $Attribute$ $Value$")
	public void show(final CommandSender target)
	{
		for (final Attribute attribute : Attribute.values())
		{
			final Double value = attributes.get(attribute);
			CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ATTRIBUTE", target, attribute.name(), value == null ? "Default" : value);
		}
	}

	@Override
	public boolean equalsDefault()
	{
		return attributes.isEmpty();
	}

	public static LivingAttributePropertiy deserialize(final Map<String, Object> map)
	{
		return new LivingAttributePropertiy(map, true);
	}

	@Override
	public Map<String, Object> serialize()
	{
		final Map<String, Object> res = new HashMap<String, Object>();
		for (final Entry<Attribute, Double> entry : attributes.entrySet())
			res.put(entry.getKey().name(), entry.getValue());
		return res;
	}

	@Override
	public void attachTo(final Entity entity, final PersistanceManager manager)
	{
		apply(entity);
		manager.watch(entity, PERSISTANCEHEADER, this);
	}
}
