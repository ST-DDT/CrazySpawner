package de.st_ddt.crazyspawner.entities.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public class EntityPropertyHelper
{

	protected final static Set<Class<? extends EntityPropertyInterface>>[] ENTITYPROPERTIES = EntityPropertyInterface.ENTITYPROPERTIES;
	static
	{
		// Properties
		for (final EntityType type : EntityType.values())
			ENTITYPROPERTIES[type.ordinal()] = new LinkedHashSet<Class<? extends EntityPropertyInterface>>();
		// Properties - VIP required to be first!
		registerEntityProperty(FallingBlockProperty.class);
		registerEntityProperty(LightningProperty.class);
		// Properties - Sorted by EntityInterfaces
		registerEntityProperty(AgeProperty.class);
		registerEntityProperty(BoatProperty.class);
		registerEntityProperty(ColorableProperty.class);
		registerEntityProperty(AlarmProperty.class);
		registerEntityProperty(CreeperProperty.class);
		registerEntityProperty(EndermanProperty.class);
		registerEntityProperty(DespawnProperty.class);
		registerEntityProperty(BurningProperty.class);
		registerEntityProperty(InvulnerableProperty.class);
		registerEntityProperty(VelocityProperty.class);
		registerEntityProperty(PassengerProperty.class);
		registerEntityProperty(ExperienceOrbProperty.class);
		registerEntityProperty(ExplosiveProperty.class);
		registerEntityProperty(FallingBlockExtendedProperty.class);
		registerEntityProperty(FireballVelocityProperty.class);
		registerEntityProperty(FireworkProperty.class);
		registerEntityProperty(HangingProperty.class);
		registerEntityProperty(HorseProperty.class);
		// InventoryHolder required?
		registerEntityProperty(IronGolemProperty.class);
		registerEntityProperty(AlarmProperty.class);
		registerEntityProperty(DroppedItemProperty.class);
		registerEntityProperty(ItemFrameProperty.class);
		registerEntityProperty(DamageProperty.class);
		registerEntityProperty(DetectionProperty.class);
		registerEntityProperty(EquipmentProperties.class);
		registerEntityProperty(HealthProperty.class);
		registerEntityProperty(LivingDespawnProperty.class);
		registerEntityProperty(NameProperty.class);
		registerEntityProperty(PeacefulProperty.class);
		registerEntityProperty(PotionProterty.class);
		registerEntityProperty(XPProperty.class);
		registerEntityProperty(MinecartProperty.class);
		registerEntityProperty(OcelotProperty.class);
		registerEntityProperty(PaintingProperty.class);
		registerEntityProperty(PigProperty.class);
		registerEntityProperty(PigZombieProperty.class);
		registerEntityProperty(DamageProperty.class);
		registerEntityProperty(ProjectileProperty.class);
		registerEntityProperty(SheepProperty.class);
		registerEntityProperty(SkeletonProperty.class);
		registerEntityProperty(SlimeProperty.class);
		registerEntityProperty(TameableProperty.class);
		registerEntityProperty(ThrownPotionProperty.class);
		registerEntityProperty(TNTPrimedProperty.class);
		registerEntityProperty(VillagerProperty.class);
		registerEntityProperty(WolfProperty.class);
		registerEntityProperty(ZombieProperty.class);
	}

	public static void registerEntityProperty(final Class<? extends EntityPropertyInterface> propertyClass)
	{
		try
		{
			checkConstructors(propertyClass);
			final EntityPropertyInterface property = propertyClass.newInstance();
			for (final EntityType type : EntityType.values())
				if (type.getEntityClass() != null && property.isApplicable(type.getEntityClass()))
					ENTITYPROPERTIES[type.ordinal()].add(propertyClass);
		}
		catch (final Exception e)
		{
			reportPropertyException(e, propertyClass);
		}
	}

	protected static void checkConstructors(final Class<? extends EntityPropertyInterface> propertyClass) throws Exception
	{
		propertyClass.getConstructor();
		propertyClass.getConstructor(ConfigurationSection.class);
		propertyClass.getConstructor(Map.class);
	}

	public static List<EntityPropertyInterface> getDefaultEntityProperties(final EntityType type)
	{
		final Set<Class<? extends EntityPropertyInterface>> properties = ENTITYPROPERTIES[type.ordinal()];
		final List<EntityPropertyInterface> res = new ArrayList<EntityPropertyInterface>(properties.size());
		for (final Class<? extends EntityPropertyInterface> property : properties)
			try
			{
				res.add(property.newInstance());
			}
			catch (final Exception e)
			{
				reportTypedPropertyException(e, type, property);
			}
		return res;
	}

	public static List<EntityPropertyInterface> getEntityPropertiesFromConfig(final EntityType type, final ConfigurationSection config)
	{
		final Set<Class<? extends EntityPropertyInterface>> properties = ENTITYPROPERTIES[type.ordinal()];
		final List<EntityPropertyInterface> res = new ArrayList<EntityPropertyInterface>(properties.size());
		for (final Class<? extends EntityPropertyInterface> property : properties)
			try
			{
				res.add(property.getConstructor(ConfigurationSection.class).newInstance(config));
			}
			catch (final Exception e)
			{
				reportTypedPropertyException(e, type, property);
			}
		return res;
	}

	public static List<EntityPropertyInterface> getEntityPropertiesFromParams(final EntityType type, final Map<String, ? extends Paramitrisable> params)
	{
		final Set<Class<? extends EntityPropertyInterface>> properties = ENTITYPROPERTIES[type.ordinal()];
		final List<EntityPropertyInterface> res = new ArrayList<EntityPropertyInterface>(properties.size());
		for (final Class<? extends EntityPropertyInterface> property : properties)
			try
			{
				res.add(property.getConstructor(Map.class).newInstance(params));
			}
			catch (final Exception e)
			{
				reportTypedPropertyException(e, type, property);
			}
		return res;
	}

	public static StringParamitrisable getCommandParams(final EntityType type, final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final StringParamitrisable nameParam = new StringParamitrisable(null);
		params.put("n", nameParam);
		params.put("name", nameParam);
		for (final EntityPropertyInterface property : getDefaultEntityProperties(type))
			property.getCommandParams(params, sender);
		return nameParam;
	}

	protected static Set<Class<? extends EntityPropertyInterface>> getAllPropertyClasses()
	{
		final Set<Class<? extends EntityPropertyInterface>> properties = new HashSet<Class<? extends EntityPropertyInterface>>();
		for (final EntityType type : EntitySpawnerHelper.getSpawnableEntityTypes())
			properties.addAll(ENTITYPROPERTIES[type.ordinal()]);
		return properties;
	}

	public static int getTotalPropertiesCount()
	{
		return getAllPropertyClasses().size();
	}

	public static int getTotalCommandParamsCount()
	{
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final ConsoleCommandSender console = Bukkit.getConsoleSender();
		for (final Class<? extends EntityPropertyInterface> propertyClass : getAllPropertyClasses())
			try
			{
				propertyClass.newInstance().getCommandParams(params, console);
			}
			catch (final Exception e)
			{
				reportPropertyException(e, propertyClass);
			}
		return new HashSet<Paramitrisable>(params.values()).size();
	}

	protected static void reportPropertyException(final Exception exception, final Class<? extends EntityPropertyInterface> propertyClass)
	{
		System.err.println("[CrazySpawner] WARNING: Serious Bug detected, please report this!");
		System.err.println("Property: " + propertyClass.getSimpleName());
		exception.printStackTrace();
	}

	protected static void reportTypedPropertyException(final Exception exception, final EntityType type, final Class<? extends EntityPropertyInterface> propertyClass)
	{
		System.err.println("[CrazySpawner] WARNING: Serious Bug detected, please report this!");
		System.err.println("EntityType: " + type.name() + ", Property: " + propertyClass.getSimpleName());
		exception.printStackTrace();
	}

	protected EntityPropertyHelper()
	{
	}
}
