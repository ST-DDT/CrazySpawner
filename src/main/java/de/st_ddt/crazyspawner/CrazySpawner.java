package de.st_ddt.crazyspawner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffectType;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyCommandReload.Reloadable;
import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.commands.CommandCreatureSpawner;
import de.st_ddt.crazyspawner.commands.CommandKill;
import de.st_ddt.crazyspawner.commands.CommandModifyEntity;
import de.st_ddt.crazyspawner.commands.CommandMountMe;
import de.st_ddt.crazyspawner.commands.CommandOverwriteEntity;
import de.st_ddt.crazyspawner.commands.CommandShowEntity;
import de.st_ddt.crazyspawner.commands.CommandSpawn;
import de.st_ddt.crazyspawner.commands.CommandSpawnList;
import de.st_ddt.crazyspawner.commands.CommandSpawnRemove;
import de.st_ddt.crazyspawner.commands.CommandTheEndAutoRespawn;
import de.st_ddt.crazyspawner.entities.CustomEntitySpawner;
import de.st_ddt.crazyspawner.entities.persistance.PersistanceManager;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyHelper;
import de.st_ddt.crazyspawner.entities.properties.EquipmentProperties;
import de.st_ddt.crazyspawner.entities.properties.PotionProterty;
import de.st_ddt.crazyspawner.listener.EntityListener;
import de.st_ddt.crazyspawner.listener.EntityPersistenceListener;
import de.st_ddt.crazyspawner.listener.PlayerListener;
import de.st_ddt.crazyspawner.tasks.TimerSpawnTask;
import de.st_ddt.crazyspawner.tasks.options.Thunder;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.Drop;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.entities.EntityMatcherHelper;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawnerHelper;
import de.st_ddt.crazyutil.metrics.Metrics;
import de.st_ddt.crazyutil.metrics.Metrics.Graph;
import de.st_ddt.crazyutil.metrics.Metrics.Plotter;
import de.st_ddt.crazyutil.modes.BooleanFalseMode;
import de.st_ddt.crazyutil.modes.DoubleMode;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;
import de.st_ddt.crazyutil.source.Permission;
import de.st_ddt.crazyutil.source.PermissionVariable;

@LocalizedVariable(variables = { "CRAZYPLUGIN" }, values = { "CRAZYSPAWNER" })
@PermissionVariable(variables = { "CRAZYPLUGIN" }, values = { "CRAZYSPAWNER" })
public class CrazySpawner extends CrazyPlugin
{

	private static CrazySpawner plugin;
	protected final Map<String, CustomEntitySpawner> customEntities = new LinkedHashMap<String, CustomEntitySpawner>();
	protected final YamlConfiguration customEntitiesConfig = new YamlConfiguration();
	protected final Set<TimerSpawnTask> timerTasks = new TreeSet<TimerSpawnTask>();
	protected final YamlConfiguration tasksConfig = new YamlConfiguration();
	protected final CustomEntitySpawner[] overwriteEntities = new CustomEntitySpawner[EntityType.values().length];
	protected final Map<Player, EntityType> creatureSelection = new HashMap<Player, EntityType>();
	protected PersistanceManager persistanceManager;
	protected double defaultAlarmRange;
	protected boolean monsterExplosionDamageEnabled;
	static
	{
		CrazyPipe.registerPipe(new CrazyPipe()
		{

			@Override
			public void execute(final CommandSender sender, final Collection<? extends ParameterData> datas, final String... pipeArgs) throws CrazyException
			{
				for (final ParameterData data : datas)
					strikeTarget(sender, data, ChatHelper.putArgsPara(sender, pipeArgs, data));
			}

			private void strikeTarget(final CommandSender sender, final ParameterData data, final String[] pipeArgs) throws CrazyException
			{
				final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
				final LocationParamitrisable location = new LocationParamitrisable(sender);
				location.addFullParams(params, "", "l", "loc", "location");
				ChatHelperExtended.readParameters(pipeArgs, params, location);
				if (location.getValue() == null)
					throw new CrazyCommandUsageException("[World] <X> <Y> <Z>");
				if (location.getValue().getWorld() == null)
					throw new CrazyCommandUsageException("<World> <X> <Y> <Z>");
				location.getValue().getWorld().strikeLightning(location.getValue());
			}

			@Override
			public void execute(final CommandSender sender, final Collection<String> datas, final boolean foo, final String... pipeArgs) throws CrazyException
			{
				final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
				final LocationParamitrisable location = new LocationParamitrisable(sender);
				location.addFullParams(params, "", "l", "loc", "location");
				ChatHelperExtended.readParameters(pipeArgs, params, location);
				if (location.getValue() == null)
					throw new CrazyCommandUsageException("[World] <X> <Y> <Z>");
				if (location.getValue().getWorld() == null)
					throw new CrazyCommandUsageException("<World> <X> <Y> <Z>");
				location.getValue().getWorld().strikeLightning(location.getValue());
			}
		}, "thunder", "strike");
	}

	public static CrazySpawner getPlugin()
	{
		return plugin;
	}

	public CrazySpawner()
	{
		super();
		registerModes();
	}

	@Localized("CRAZYSPAWNER.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(new DoubleMode(this, "defaultAlarmRange")
		{

			@Override
			public Double getValue()
			{
				return defaultAlarmRange;
			}

			@Override
			@Permission("crazyspawner.mode.defaultAlarmRange")
			public void setValue(final Double newValue) throws CrazyException
			{
				defaultAlarmRange = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(new BooleanFalseMode(this, "monsterExplosionDamageEnabled")
		{

			@Override
			public Boolean getValue()
			{
				return monsterExplosionDamageEnabled;
			}

			@Override
			@Permission("crazyspawner.mode.monsterExplosionDamageEnabled")
			public void setValue(final Boolean newValue) throws CrazyException
			{
				monsterExplosionDamageEnabled = newValue;
				saveConfiguration();
			}
		});
	}

	private void registerHooks()
	{
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(this, creatureSelection), this);
		pm.registerEvents(new EntityListener(this, overwriteEntities), this);
		pm.registerEvents(new EntityPersistenceListener(this, persistanceManager), this);
	}

	private void registerCommands()
	{
		getCommand("crazyspawn").setExecutor(new CommandSpawn(this));
		getCommand("crazykill").setExecutor(new CommandKill(this));
		getCommand("crazycreaturespawner").setExecutor(new CommandCreatureSpawner(this, creatureSelection));
		getCommand("crazytheendautorespawn").setExecutor(new CommandTheEndAutoRespawn(this));
		getCommand("mountme").setExecutor(new CommandMountMe(this));
		mainCommand.addSubCommand(new CommandModifyEntity(this), "me", "modentity", "modifyentity");
		mainCommand.addSubCommand(new CommandShowEntity(this), "se", "showentity", "entityinfo");
		mainCommand.addSubCommand(new CommandOverwriteEntity(this), "oe", "overwriteentity");
		mainCommand.addSubCommand(new CommandSpawnList(this), "l", "list");
		mainCommand.addSubCommand(new CommandSpawnRemove(this), "rem", "remove");
	}

	private void registerMetrics()
	{
		final boolean metricsEnabled = getConfig().getBoolean("metrics.enabled", true);
		getConfig().set("metrics.enabled", metricsEnabled);
		if (!metricsEnabled)
			return;
		try
		{
			final Metrics metrics = new Metrics(this);
			final Graph pluginStats = metrics.createGraph("Plugin stats");
			pluginStats.addPlotter(new Plotter("CustomEntities")
			{

				@Override
				public int getValue()
				{
					return customEntities.size();
				}
			});
			pluginStats.addPlotter(new Plotter("SpawnTasks")
			{

				@Override
				public int getValue()
				{
					return timerTasks.size();
				}
			});
			final Graph customEntityCount = metrics.createGraph("Custom Entities");
			for (final EntityType type : EntitySpawnerHelper.getSpawnableEntityTypes())
				customEntityCount.addPlotter(new Plotter(type.name())
				{

					@Override
					public int getValue()
					{
						int i = 0;
						for (final CustomEntitySpawner customEntity : customEntities.values())
							if (customEntity.getType() == type)
								i++;
						return i;
					}
				});
			metrics.start();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		persistanceManager = new PersistanceManager(new File(getDataFolder(), "StoredEntities"));
		// Static code initialization.
		EntityMatcherHelper.class.getName();
		EntitySpawnerHelper.class.getName();
		NamedEntitySpawnerHelper.class.getName();
		NamedEntitySpawnerParamitrisable.class.getName();
		CustomEntitySpawner.class.getName();
		super.onLoad();
	}

	@Override
	@Localized({ "CRAZYSPAWNER.SPAWNABLEENTITIES.OPTIONS $SupportedTypes$ $AvailableOptionGroups$ $AvailableOptions$", "CRAZYSPAWNER.SPAWNABLEENTITIES.AVAILABLE $Count$" })
	public void onEnable()
	{
		registerHooks();
		super.onEnable();
		if (isUpdated)
		{
			saveExamples();
			final ConsoleCommandSender console = Bukkit.getConsoleSender();
			if (VersionComparator.compareVersions(previousVersion, "3.7") == -1)
			{
				// DefaultCreatures
				// - Spider_Skeleton
				final CustomEntitySpawner spiderSkeleton = new CustomEntitySpawner("Spider_Skeleton", EntityType.SPIDER, console, "passenger:SKELETON");
				customEntities.put(spiderSkeleton.getName(), spiderSkeleton);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(spiderSkeleton);
				// - Zombie_Skeleton
				final CustomEntitySpawner spiderZombie = new CustomEntitySpawner("Spider_Zombie", EntityType.SPIDER, console, "passenger:ZOMBIE");
				customEntities.put(spiderZombie.getName(), spiderZombie);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(spiderZombie);
				// - Diamond_Zombie
				final CustomEntitySpawner diamondZombie = new CustomEntitySpawner("Diamond_Zombie", EntityType.ZOMBIE, console, "boots:DIAMOND_BOOTS", "bootsdropchance:0.01", "leggings:DIAMOND_LEGGINGS", "leggingsdropchance:0.01", "chestplate:DIAMOND_CHESTPLATE", "chestplatedropchance:0.01", "helmet:DIAMOND_HELMET", "helmetdropchance:0.01", "iteminhand:DIAMOND_SWORD", "iteminhanddropchance:0.01");
				customEntities.put(diamondZombie.getName(), diamondZombie);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(diamondZombie);
				// - Giant
				final CustomEntitySpawner giant = new CustomEntitySpawner("Giant", EntityType.GIANT);
				customEntities.put(giant.getName(), giant);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(giant);
				// - Healthy_Giant
				final CustomEntitySpawner healthyGiant = new CustomEntitySpawner("Healthy_Giant", EntityType.GIANT, console, "maxhealth:200");
				customEntities.put(healthyGiant.getName(), healthyGiant);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(healthyGiant);
				// - Spider_Diamond_Zombie
				final CustomEntitySpawner spiderDiamondZombie = new CustomEntitySpawner("Spider_Diamond_Zombie", EntityType.SPIDER, console, "passenger:Diamond_Zombie");
				customEntities.put(spiderDiamondZombie.getName(), spiderDiamondZombie);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(spiderDiamondZombie);
				saveConfiguration();
			}
			if (VersionComparator.compareVersions(previousVersion, "3.11") == -1)
			{
				// - Speedy_Baby_Zombie
				final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
				boots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
				final LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
				meta.setColor(Color.RED);
				boots.setItemMeta(meta);
				final CustomEntitySpawner speedyZombie = new CustomEntitySpawner("Speedy_Baby_Zombie", EntityType.ZOMBIE, console, "baby:true");
				speedyZombie.addEntityProperty(new EquipmentProperties(boots, 0.5F, null, 0, null, 0, null, 0, null, 0, null, null));
				speedyZombie.addEntityProperty(new PotionProterty(new PotionEffectType[] { PotionEffectType.SPEED }, new int[] { 5 }));
				customEntities.put(speedyZombie.getName(), speedyZombie);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(speedyZombie);
				saveConfiguration();
			}
			if (VersionComparator.compareVersions(previousVersion, "3.11.1") == -1)
			{
				getConfig().set("example", null);
				saveConfiguration();
			}
			if (VersionComparator.compareVersions(previousVersion, "3.15") == -1)
			{
				// - Healthy_Diamond_Zombie
				final ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
				final ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
				final ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
				chestplate.setDurability((short) 3);
				chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 5);
				chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 5);
				chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
				chestplate.addUnsafeEnchantment(Enchantment.THORNS, 3);
				final ItemMeta meta = chestplate.getItemMeta();
				meta.setDisplayName("Holy Chestplate of the Goddes");
				final List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.RED + "Blessed by the goddess kiss");
				lore.add("Manufactured by the best dwarfs known");
				meta.setLore(lore);
				chestplate.setItemMeta(meta);
				final ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
				final ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
				final List<Drop> drops = new ArrayList<Drop>();
				drops.add(new Drop(new ItemStack(Material.DIAMOND, 2), 0.5F));
				drops.add(new Drop(new ItemStack(Material.DIAMOND, 3), 0.5F));
				drops.add(new Drop(new ItemStack(Material.GOLD_INGOT, 2), 0.5F));
				drops.add(new Drop(new ItemStack(Material.GOLD_INGOT, 3), 0.5F));
				drops.add(new Drop(new ItemStack(Material.EMERALD, 2), 0.5F));
				drops.add(new Drop(boots, 1F));
				drops.add(new Drop(leggings, 1F));
				drops.add(new Drop(chestplate, 1F));
				drops.add(new Drop(helmet, 1F));
				drops.add(new Drop(sword, 1F));
				final CustomEntitySpawner healthyDiamondZombie = new CustomEntitySpawner("Healthy_Diamond_Zombie", EntityType.ZOMBIE, console, "customName:&3Diamond_Zombie", "showNameAboveHead:true", "showHealth:true", "maxHealth:100", "minDamage:3", "maxDamage:7", "minXP:10", "maxXP:20");
				healthyDiamondZombie.addEntityProperty(new EquipmentProperties(boots, 1F, leggings, 1F, chestplate, 1F, helmet, 1F, sword, 1F, drops, false));
				customEntities.put(healthyDiamondZombie.getName(), healthyDiamondZombie);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(healthyDiamondZombie);
				saveConfiguration();
			}
		}
		mainCommand.getReloadCommand().addReloadable(new Reloadable()
		{

			@Override
			@Localized("CRAZYSPAWNER.COMMAND.SPAWNABLEENTITIES.RELOADED")
			public void reload(final CommandSender sender) throws CrazyException
			{
				loadCustomEntities();
				saveCustomEntities();
				sendLocaleMessage("COMMAND.SPAWNABLEENTITIES.RELOADED", sender);
			}

			@Override
			@Permission("crazyspawner.reload.entities")
			public boolean hasAccessPermission(final CommandSender sender)
			{
				return sender.hasPermission("crazyspawner.reload.entities") || sender.hasPermission("crazyspawner.reload.*");
			}
		}, "e", "ce", "entities", "customentities");
		mainCommand.getReloadCommand().addReloadable(new Reloadable()
		{

			@Override
			@Localized("CRAZYSPAWNER.COMMAND.SPAWNTASKS.RELOADED")
			public void reload(final CommandSender sender) throws CrazyException
			{
				loadSpawnTasks();
				saveSpawnTasks();
				sendLocaleMessage("COMMAND.SPAWNABLEENTITIES.RELOADED", sender);
			}

			@Override
			@Permission("crazyspawner.reload.spawntasks")
			public boolean hasAccessPermission(final CommandSender sender)
			{
				return sender.hasPermission("crazyspawner.reload.spawntasks") || sender.hasPermission("crazyspawner.reload.*");
			}
		}, "t", "st", "spawntasks");
		sendLocaleMessage("SPAWNABLEENTITIES.OPTIONS", Bukkit.getConsoleSender(), EntitySpawnerHelper.getTotalSpawnableEntityTypeCount(), EntityPropertyHelper.getTotalPropertiesCount(), EntityPropertyHelper.getTotalCommandParamsCount());
		sendLocaleMessage("SPAWNABLEENTITIES.AVAILABLE", Bukkit.getConsoleSender(), NamedEntitySpawner.NAMEDENTITYSPAWNERS.size());
		registerCommands();
		registerMetrics();
	}

	protected void saveExamples()
	{
		final File exampleFolder = new File(getDataFolder(), "example");
		exampleFolder.mkdirs();
		// Example - Entities (Properties)
		final YamlConfiguration allCustomEntityProperties = new YamlConfiguration();
		allCustomEntityProperties.options().header("CrazySpawner example EntityALL.yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Entity.yml\n" + "Custom entities have to be defined inside config.yml");
		for (final EntityType type : EntitySpawnerHelper.getSpawnableEntityTypes())
		{
			final YamlConfiguration customEntityProperties = new YamlConfiguration();
			customEntityProperties.options().header("CrazySpawner example Entity" + type.name() + ".yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Entity.yml\n" + "Custom entities have to be defined inside config.yml");
			final CustomEntitySpawner spawner = new CustomEntitySpawner(type);
			spawner.dummySave(customEntityProperties, "example" + type.name() + ".");
			spawner.dummySave(allCustomEntityProperties, "exampleALL.");
			customEntityProperties.set("example" + type.name() + ".type", type.name());
			saveExampleFile(exampleFolder, "Entity" + type.name(), customEntityProperties);
		}
		saveExampleFile(exampleFolder, "EntityALL", allCustomEntityProperties);
		// Example - EntityType (supported only)
		final YamlConfiguration entityTypes = new YamlConfiguration();
		entityTypes.set("exampleEntityType", EnumParamitrisable.getEnumNames(EntitySpawnerHelper.getSpawnableEntityTypes()));
		saveExampleFile(exampleFolder, "EntityType", entityTypes);
		// Example - CustomEntityNames
		final YamlConfiguration customTypes = new YamlConfiguration();
		customTypes.set("exampleCustomEntityNames", new ArrayList<String>(NamedEntitySpawner.NAMEDENTITYSPAWNERS.keySet()));
		saveExampleFile(exampleFolder, "CustomEntityNames", customTypes);
		// Example - Item
		final YamlConfiguration item = new YamlConfiguration();
		item.options().header("CrazySpawner example Item.yml\n" + "For more information visit\n" + "https://github.com/ST-DDT/Crazy/blob/master/CrazySpawner/docs/example/Item.yml\n" + "Items have to be defined inside config.yml (in the custom customEntity inventory slots)");
		item.set("exampleItem.type", "Material");
		item.set("exampleItem.damage", "short (0 (full) - 528 (broken, upper limit may differ (mostly below)))");
		item.set("exampleItem.amount", "int (1-64)");
		item.set("exampleItem.meta.==", "ItemMeta");
		item.set("exampleItem.meta.meta-type", "UNSPECIFIC");
		item.set("exampleItem.meta.display-name", "String");
		item.set("exampleItem.meta.lore", new String[] { "Line1", "Line2", "..." });
		item.set("exampleItem.meta.enchants.ENCHANTMENT1", "int (1-255)");
		item.set("exampleItem.meta.enchants.ENCHANTMENT2", "int (1-255)");
		item.set("exampleItem.meta.enchants.ENCHANTMENTx", "int (1-255)");
		saveExampleFile(exampleFolder, "Item", item);
		// Example - Enchantment
		final YamlConfiguration enchantments = new YamlConfiguration();
		final List<String> exampleEnchantments = new ArrayList<String>();
		for (final Enchantment enchantment : Enchantment.values())
			if (enchantment != null)
				exampleEnchantments.add(enchantment.getName());
		enchantments.set("exampleEnchantment", exampleEnchantments);
		saveExampleFile(exampleFolder, "Enchantment", enchantments);
		// Example - PotionEffect
		final YamlConfiguration potionEffects = new YamlConfiguration();
		final List<String> examplePotionEffects = new ArrayList<String>();
		for (final PotionEffectType potionEffect : PotionEffectType.values())
			if (potionEffect != null)
				examplePotionEffects.add(potionEffect.getName());
		potionEffects.set("examplePotionEffect", examplePotionEffects);
		saveExampleFile(exampleFolder, "PotionEffect", potionEffects);
		// Example - FireworkMeta
		final YamlConfiguration firework = new YamlConfiguration();
		firework.set("exampleFireworkMeta.==", "ItemMeta");
		firework.set("exampleFireworkMeta.meta-type", "FIREWORK");
		final List<Map<String, Object>> effectsList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 2; i++)
		{
			final Map<String, Object> effectMap = new LinkedHashMap<String, Object>();
			effectMap.put("==", "Firework");
			effectMap.put("flicker", "boolean (true/false)");
			effectMap.put("trail", "boolean (true/false)");
			for (final String colorSection : new String[] { "colors", "fade-colors" })
			{
				final List<Map<String, Object>> colorList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < 2; j++)
				{
					final Map<String, Object> colorMap = new LinkedHashMap<String, Object>();
					colorMap.put("==", "Color");
					colorMap.put("RED", "int (0-255)");
					colorMap.put("GREEN", "int (0-255)");
					colorMap.put("BLUE", "int (0-255)");
					colorList.add(colorMap);
				}
				effectMap.put(colorSection, colorList);
			}
			effectMap.put("type", ChatHelper.listingString(" | ", EnumParamitrisable.getEnumNames(FireworkEffect.Type.values())));
			effectsList.add(effectMap);
		}
		firework.set("exampleFireworkMeta.firework-effects", effectsList);
		firework.set("exampleFireworkMeta.power", "int (0-127)");
		saveExampleFile(exampleFolder, "FireworkMeta", firework);
		// Example - Enums
		saveExampleEnum(exampleFolder, Art.values(), "Art");
		saveExampleEnum(exampleFolder, BlockFace.values(), "BlockFace");
		saveExampleEnum(exampleFolder, DyeColor.values(), "DyeColor");
		saveExampleEnum(exampleFolder, Horse.Color.values(), "HorseColor");
		saveExampleEnum(exampleFolder, Horse.Style.values(), "HorseStyle");
		saveExampleEnum(exampleFolder, Horse.Variant.values(), "HorseVariant");
		saveExampleEnum(exampleFolder, Material.values(), "Material");
		saveExampleEnum(exampleFolder, Ocelot.Type.values(), "CatType");
		saveExampleEnum(exampleFolder, Rotation.values(), "Rotation");
		saveExampleEnum(exampleFolder, Skeleton.SkeletonType.values(), "SkeletonType");
		saveExampleEnum(exampleFolder, Thunder.values(), "Thunder");
		saveExampleEnum(exampleFolder, Villager.Profession.values(), "Profession");
	}

	protected <A extends Enum<A>> void saveExampleEnum(final File exampleFolder, final A[] values, final String name)
	{
		final YamlConfiguration config = new YamlConfiguration();
		config.set("example" + name, EnumParamitrisable.getEnumNames(values).toArray());
		saveExampleFile(exampleFolder, name, config);
	}

	protected void saveExampleFile(final File exampleFolder, final String name, final YamlConfiguration config)
	{
		try
		{
			config.save(new File(exampleFolder, name + ".yml"));
		}
		catch (final IOException e)
		{
			System.err.println("[CrazySpawner] Could not save example " + name + ".yml.");
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void load()
	{
		loadCustomEntities();
		super.load();
		loadSpawnTasks();
	}

	@Override
	public void loadConfiguration()
	{
		final ConfigurationSection config = getConfig();
		// CustomEntities (Deprecated)
		final ConfigurationSection customEntityConfig = config.getConfigurationSection("customEntities");
		if (customEntityConfig != null)
		{
			for (final String key : customEntityConfig.getKeys(false))
				try
				{
					final CustomEntitySpawner customEntity = new CustomEntitySpawner(customEntityConfig.getConfigurationSection(key));
					customEntities.put(customEntity.getName(), customEntity);
					NamedEntitySpawnerHelper.registerNamedEntitySpawner(customEntity);
				}
				catch (final IllegalArgumentException e)
				{
					System.err.println("Could not load customEntity " + key);
					System.err.println(e.getMessage());
				}
			config.set("customEntities", null);
		}
		// Tasks (Deprecated)
		final ConfigurationSection taskConfig = config.getConfigurationSection("tasks");
		if (taskConfig != null)
		{
			for (final String key : taskConfig.getKeys(false))
				try
				{
					timerTasks.add(new TimerSpawnTask(this, taskConfig.getConfigurationSection(key)));
				}
				catch (final IllegalArgumentException e)
				{
					System.err.println("[CrazySpawner] Could not load TimerTask " + key + ".");
					System.err.println(e.getMessage());
				}
			config.set("tasks", null);
		}
		// OverwriteEntities
		for (final EntityType type : EntityType.values())
			if (type.getEntityClass() != null && LivingEntity.class.isAssignableFrom(type.getEntityClass()))
			{
				final NamedEntitySpawner spawner = NamedEntitySpawnerHelper.getNamedEntitySpawner(config.getString("overwriteEntities." + type.name(), null));
				if (spawner == null)
					overwriteEntities[type.ordinal()] = null;
				else if (spawner.getType() == type)
					if (spawner instanceof CustomEntitySpawner)
						overwriteEntities[type.ordinal()] = (CustomEntitySpawner) spawner;
					else
					{
						System.err.println("Could not use " + spawner.getName() + " to overwrite default " + type.name() + " entities (Only custom entities allowed)!");
						overwriteEntities[type.ordinal()] = null;
					}
				else
				{
					System.err.println("Could not use " + spawner.getName() + " to overwrite default " + type.name() + " entities (Wrong EntityType)!");
					overwriteEntities[type.ordinal()] = null;
				}
			}
			else
				overwriteEntities[type.ordinal()] = null;
		defaultAlarmRange = config.getDouble("defaultAlarmRange", 10);
		monsterExplosionDamageEnabled = config.getBoolean("monsterExplosionDamageEnabled", true);
	}

	@Localized("CRAZYSPAWNER.SPAWNABLEENTITIES.LOADED $Count$")
	public void loadCustomEntities()
	{
		final File customEntitiesFile = new File(getDataFolder(), "CustomEntities.yml");
		if (customEntitiesFile.exists())
			try
			{
				customEntitiesConfig.load(customEntitiesFile);
			}
			catch (final Exception e)
			{
				System.err.println("[CrazySpawner] Could not load CustomEntities.yml.");
				System.err.println(e.getMessage());
			}
		customEntities.clear();
		for (final String key : customEntitiesConfig.getKeys(false))
			try
			{
				final CustomEntitySpawner customEntity = new CustomEntitySpawner(customEntitiesConfig.getConfigurationSection(key));
				customEntities.put(customEntity.getName(), customEntity);
				NamedEntitySpawnerHelper.registerNamedEntitySpawner(customEntity);
			}
			catch (final Exception e)
			{
				System.err.println("[CrazySpawner] Could not load CustomEntity " + key);
				System.err.println(e.getMessage());
			}
		sendLocaleMessage("SPAWNABLEENTITIES.LOADED", Bukkit.getConsoleSender(), customEntities.size());
	}

	public void loadSpawnTasks()
	{
		final File tasksFile = new File(getDataFolder(), "Tasks.yml");
		if (tasksFile.exists())
			try
			{
				tasksConfig.load(tasksFile);
			}
			catch (final Exception e)
			{
				System.err.println("[CrazySpawner] Could not load CustomEntities.yml.");
				System.err.println(e.getMessage());
			}
		for (final TimerSpawnTask task : timerTasks)
			task.cancel();
		timerTasks.clear();
		final ConfigurationSection taskConfig = tasksConfig.getConfigurationSection("timerTasks");
		if (taskConfig != null)
			for (final String key : taskConfig.getKeys(false))
				try
				{
					timerTasks.add(new TimerSpawnTask(this, taskConfig.getConfigurationSection(key)));
				}
				catch (final IllegalArgumentException e)
				{
					System.err.println("[CrazySpawner] Could not load TimerTask " + key + ".");
					System.err.println(e.getMessage());
				}
		for (final TimerSpawnTask task : timerTasks)
			task.start(100);
	}

	@Override
	public void save()
	{
		saveCustomEntities();
		super.save();
		saveSpawnTasks();
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		// OverwriteEntities
		for (final EntityType type : EntityType.values())
		{
			final NamedEntitySpawner spawner = overwriteEntities[type.ordinal()];
			if (spawner == null)
				config.set("overwriteEntities." + type.name(), null);
			else
				config.set("overwriteEntities." + type.name(), spawner.getName());
		}
		config.set("defaultAlarmRange", defaultAlarmRange);
		config.set("monsterExplosionDamageEnabled", monsterExplosionDamageEnabled);
		super.saveConfiguration();
	}

	public void saveCustomEntities()
	{
		for (final String key : new ArrayList<String>(customEntitiesConfig.getKeys(false)))
			customEntitiesConfig.set(key, null);
		for (final CustomEntitySpawner customEntity : customEntities.values())
			customEntity.save(customEntitiesConfig, customEntity.getName() + ".");
		try
		{
			getDataFolder().mkdirs();
			customEntitiesConfig.save(new File(getDataFolder(), "CustomEntities.yml"));
		}
		catch (final Exception e)
		{
			System.err.println("[CrazySpawner] Could not save CustomEntities.yml.");
			System.err.println(e.getMessage());
		}
	}

	public void saveSpawnTasks()
	{
		if (timerTasks.size() == 0)
			tasksConfig.set("timerTasks", new HashMap<String, Object>(0));
		else
		{
			tasksConfig.set("timerTasks", null);
			int i = 0;
			for (final TimerSpawnTask task : timerTasks)
				task.save(tasksConfig, "timerTasks.t" + i++ + ".");
		}
		try
		{
			getDataFolder().mkdirs();
			tasksConfig.save(new File(getDataFolder(), "Tasks.yml"));
		}
		catch (final Exception e)
		{
			System.err.println("[CrazySpawner] Could not save Tasks.yml.");
			System.err.println(e.getMessage());
		}
	}

	public void addCustomEntity(final CustomEntitySpawner customEntity, final String... aliases)
	{
		customEntities.put(customEntity.getName(), customEntity);
		NamedEntitySpawnerHelper.registerNamedEntitySpawner(customEntity, aliases);
		saveCustomEntities();
	}

	public Map<String, CustomEntitySpawner> getCustomEntities()
	{
		return customEntities;
	}

	public void removeCustomEntity(final CustomEntitySpawner customEntity)
	{
		customEntities.remove(customEntity);
		saveCustomEntities();
	}

	public void addSpawnTask(final TimerSpawnTask task)
	{
		timerTasks.add(task);
		saveSpawnTasks();
	}

	public Set<TimerSpawnTask> getTasks()
	{
		return timerTasks;
	}

	public void removeSpawnTask(final TimerSpawnTask task)
	{
		timerTasks.remove(task);
		saveSpawnTasks();
	}

	public final CustomEntitySpawner[] getOverwriteEntities()
	{
		return overwriteEntities;
	}

	public PersistanceManager getPersistanceManager()
	{
		return persistanceManager;
	}

	public final double getDefaultAlarmRange()
	{
		return defaultAlarmRange;
	}

	public final boolean isMonsterExplosionDamageEnabled()
	{
		return monsterExplosionDamageEnabled;
	}
}
