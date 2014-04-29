package de.st_ddt.crazyspawner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.commands.CommandCreatureSpawner;
import de.st_ddt.crazyspawner.commands.CommandCustomEntityClone;
import de.st_ddt.crazyspawner.commands.CommandCustomEntityCreate;
import de.st_ddt.crazyspawner.commands.CommandCustomEntityModify;
import de.st_ddt.crazyspawner.commands.CommandCustomEntityShow;
import de.st_ddt.crazyspawner.commands.CommandKill;
import de.st_ddt.crazyspawner.commands.CommandMountMe;
import de.st_ddt.crazyspawner.commands.CommandOverwriteEntity;
import de.st_ddt.crazyspawner.commands.CommandSpawn;
import de.st_ddt.crazyspawner.commands.CommandSpawnList;
import de.st_ddt.crazyspawner.commands.CommandSpawnRemove;
import de.st_ddt.crazyspawner.commands.CommandTheEndAutoRespawn;
import de.st_ddt.crazyspawner.entities.persistance.PersistanceManager;
import de.st_ddt.crazyspawner.entities.properties.EntityPropertyHelper;
import de.st_ddt.crazyspawner.entities.spawners.NamedParentedSpawner;
import de.st_ddt.crazyspawner.entities.spawners.player.BasicPlayerSpawner;
import de.st_ddt.crazyspawner.entities.spawners.player.NamedPlayerSpawner;
import de.st_ddt.crazyspawner.entities.spawners.player.RandomPlayerSpawner;
import de.st_ddt.crazyspawner.entities.util.AttributeHelper;
import de.st_ddt.crazyspawner.listener.EntityListener;
import de.st_ddt.crazyspawner.listener.EntityPersistenceListener;
import de.st_ddt.crazyspawner.listener.PlayerListener;
import de.st_ddt.crazyspawner.tasks.TimerSpawnTask;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.NamesHelper;
import de.st_ddt.crazyutil.compatibility.CompatibilityLoader;
import de.st_ddt.crazyutil.datas.ParameterData;
import de.st_ddt.crazyutil.entities.ApplyableEntitySpawner;
import de.st_ddt.crazyutil.entities.ApplyableEntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.EntitySpawnerHelper;
import de.st_ddt.crazyutil.entities.NamedEntitySpawner;
import de.st_ddt.crazyutil.entities.NamedEntitySpawnerHelper;
import de.st_ddt.crazyutil.metrics.Metrics;
import de.st_ddt.crazyutil.metrics.Metrics.Graph;
import de.st_ddt.crazyutil.metrics.Metrics.Plotter;
import de.st_ddt.crazyutil.modes.BooleanFalseMode;
import de.st_ddt.crazyutil.modes.DoubleMode;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.reloadable.Reloadable;
import de.st_ddt.crazyutil.resources.ResourceHelper;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;
import de.st_ddt.crazyutil.source.Permission;
import de.st_ddt.crazyutil.source.PermissionVariable;

@LocalizedVariable(variables = { "CRAZYPLUGIN" }, values = { "CRAZYSPAWNER" })
@PermissionVariable(variables = { "CRAZYPLUGIN" }, values = { "CRAZYSPAWNER" })
public class CrazySpawner extends CrazyPlugin
{

	private static CrazySpawner plugin;
	protected final Map<String, NamedParentedSpawner> customEntities = new LinkedHashMap<String, NamedParentedSpawner>();
	protected final YamlConfiguration customEntitiesConfig = new YamlConfiguration();
	protected final Set<TimerSpawnTask> timerTasks = new TreeSet<TimerSpawnTask>();
	protected final YamlConfiguration tasksConfig = new YamlConfiguration();
	protected final ApplyableEntitySpawner[] overwriteEntities = new ApplyableEntitySpawner[EntityType.values().length];
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

	@Override
	protected void registerHooks()
	{
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(this, creatureSelection), this);
		pm.registerEvents(new EntityListener(this, overwriteEntities), this);
		pm.registerEvents(new EntityPersistenceListener(this, persistanceManager), this);
	}

	@Override
	protected void registerCommands()
	{
		super.registerCommands();
		registerCommand("crazyspawn", new CommandSpawn(this));
		registerCommand("crazykill", new CommandKill(this));
		registerCommand("crazycreaturespawner", new CommandCreatureSpawner(this, creatureSelection));
		registerCommand("crazytheendautorespawn", new CommandTheEndAutoRespawn(this));
		registerCommand("mountme", new CommandMountMe(this));
		mainCommand.addSubCommand(new CommandCustomEntityCreate(this), "ce", "createentity");
		mainCommand.addSubCommand(new CommandCustomEntityClone(this), "cloneentity");
		mainCommand.addSubCommand(new CommandCustomEntityModify(this), "me", "modentity", "modifyentity");
		mainCommand.addSubCommand(new CommandCustomEntityShow(this), "se", "showentity", "entityinfo");
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
						for (final NamedParentedSpawner customEntity : customEntities.values())
							if (customEntity.getEntityType() == type)
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
	public void initialize()
	{
		super.initialize();
		plugin = this;
		CompatibilityLoader.loadCompatibilityProvider(this);
		persistanceManager = new PersistanceManager(new File(getDataFolder(), "StoredEntities"));
		if (BasicPlayerSpawner.initialize())
		{
			EntitySpawnerHelper.registerEntitySpawner(new NamedPlayerSpawner());
			NamedEntitySpawnerHelper.registerNamedEntitySpawner(new RandomPlayerSpawner());
		}
		// Static code initialization.
		// TODO does this work?
		EntitySpawnerHelper.class.getName();
		NamedEntitySpawnerHelper.class.getName();
		AttributeHelper.initialize();
		EntityPropertyHelper.class.getName();
	}

	@Override
	@Localized({ "CRAZYSPAWNER.SPAWNABLEENTITIES.OPTIONS $SupportedTypes$ $AvailableOptionGroups$ $AvailableOptions$", "CRAZYSPAWNER.SPAWNABLEENTITIES.AVAILABLE $Count$" })
	public void enable()
	{
		super.enable();
		if (isUpdated)
		{
			CrazySpawnerExamples.saveExampleFiles(getDataFolder());
			CrazySpawnerExamples.saveExampleEntities(customEntities, previousVersion);
		}
		final File namesFile = new File(getDataFolder(), "Names.txt");
		if (!namesFile.exists())
			ResourceHelper.saveResource(plugin, "/Names.txt", namesFile);
		NamesHelper.loadNames(namesFile);
		final Reloadable customEntityReloadable = new Reloadable()
		{

			@Override
			@Localized("CRAZYSPAWNER.COMMAND.SPAWNABLEENTITIES.RELOADED")
			public void reload(final CommandSender sender)
			{
				loadCustomEntities();
				saveCustomEntities();
				sendLocaleMessage("COMMAND.SPAWNABLEENTITIES.RELOADED", sender);
			}

			@Override
			@Permission("crazyspawner.reload.entities")
			public boolean hasReloadPermission(final CommandSender sender)
			{
				return sender.hasPermission("crazyspawner.reload.entities") || sender.hasPermission("crazyspawner.reload.*");
			}

			@Override
			@Localized("CRAZYSPAWNER.COMMAND.SPAWNABLEENTITIES.SAVED")
			public void save(final CommandSender sender)
			{
				saveCustomEntities();
				sendLocaleMessage("COMMAND.SPAWNABLEENTITIES.SAVED", sender);
			}

			@Override
			@Permission("crazyspawner.save.entities")
			public boolean hasSavePermission(final CommandSender sender)
			{
				return sender.hasPermission("crazyspawner.save.entities") || sender.hasPermission("crazyspawner.save.*");
			}
		};
		for (final String alias : new String[] { "e", "ce", "entities", "customentities" })
			reloadables.put(alias, customEntityReloadable);
		final Reloadable spawnTasksReloadable = new Reloadable()
		{

			@Override
			@Localized("CRAZYSPAWNER.COMMAND.SPAWNTASKS.RELOADED")
			public void reload(final CommandSender sender)
			{
				loadSpawnTasks();
				saveSpawnTasks();
				sendLocaleMessage("COMMAND.SPAWNABLEENTITIES.RELOADED", sender);
			}

			@Override
			@Permission("crazyspawner.reload.spawntasks")
			public boolean hasReloadPermission(final CommandSender sender)
			{
				return sender.hasPermission("crazyspawner.reload.spawntasks") || sender.hasPermission("crazyspawner.reload.*");
			}

			@Override
			@Localized("CRAZYSPAWNER.COMMAND.SPAWNTASKS.SAVED")
			public void save(final CommandSender sender)
			{
				saveSpawnTasks();
				sendLocaleMessage("COMMAND.SPAWNABLEENTITIES.SAVED", sender);
			}

			@Override
			@Permission("crazyspawner.save.spawntasks")
			public boolean hasSavePermission(final CommandSender sender)
			{
				return sender.hasPermission("crazyspawner.save.spawntasks") || sender.hasPermission("crazyspawner.save.*");
			}
		};
		for (final String alias : new String[] { "t", "st", "spawntasks" })
			reloadables.put(alias, spawnTasksReloadable);
		sendLocaleMessage("SPAWNABLEENTITIES.OPTIONS", Bukkit.getConsoleSender(), EntitySpawnerHelper.getTotalSpawnableEntityTypeCount(), EntityPropertyHelper.getTotalPropertiesCount(), EntityPropertyHelper.getTotalCommandParamsCount());
		sendLocaleMessage("SPAWNABLEENTITIES.AVAILABLE", Bukkit.getConsoleSender(), NamedEntitySpawner.NAMEDENTITYSPAWNERS.size());
		registerCommands();
		registerMetrics();
	}

	@Override
	public void load()
	{
		loadCustomEntities();
		super.load();
		loadSpawnTasks();
	}

	@Override
	public void loadConfiguration(final ConfigurationSection config)
	{
		super.loadConfiguration(config);
		// OverwriteEntities
		for (final EntityType type : EntityType.values())
			if (type.getEntityClass() != null && LivingEntity.class.isAssignableFrom(type.getEntityClass()))
			{
				final NamedEntitySpawner spawner = NamedEntitySpawnerHelper.getNamedEntitySpawner(config.getString("overwriteEntities." + type.name(), null));
				if (spawner == null)
					overwriteEntities[type.ordinal()] = null;
				else if (spawner.getEntityType() == type)
				{
					final ApplyableEntitySpawner applyableSpawner = ApplyableEntitySpawnerHelper.wrapSpawner(spawner);
					if (applyableSpawner == null)
					{
						System.err.println("Could not use " + spawner.getName() + " to overwrite default " + type.name() + " entities (Spawner is not applicable to existing entities)!");
						overwriteEntities[type.ordinal()] = null;
					}
					else
						overwriteEntities[type.ordinal()] = applyableSpawner;
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
				final NamedParentedSpawner customEntity = new NamedParentedSpawner(customEntitiesConfig.getConfigurationSection(key));
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
	public void saveConfiguration(final ConfigurationSection config)
	{
		super.saveConfiguration(config);
		// OverwriteEntities
		for (final EntityType type : EntityType.values())
		{
			final ApplyableEntitySpawner spawner = overwriteEntities[type.ordinal()];
			if (spawner == null || !(spawner instanceof NamedEntitySpawner))
				config.set("overwriteEntities." + type.name(), null);
			else
				config.set("overwriteEntities." + type.name(), ((NamedEntitySpawner) spawner).getName());
		}
		config.set("defaultAlarmRange", defaultAlarmRange);
		config.set("monsterExplosionDamageEnabled", monsterExplosionDamageEnabled);
	}

	public void saveCustomEntities()
	{
		for (final String key : new ArrayList<String>(customEntitiesConfig.getKeys(false)))
			customEntitiesConfig.set(key, null);
		for (final NamedParentedSpawner customEntity : customEntities.values())
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

	public void addCustomEntity(final NamedParentedSpawner customEntity)
	{
		customEntities.put(customEntity.getName(), customEntity);
		NamedEntitySpawnerHelper.registerNamedEntitySpawner(customEntity);
		saveCustomEntities();
	}

	public Map<String, NamedParentedSpawner> getCustomEntities()
	{
		return customEntities;
	}

	public void removeCustomEntity(final NamedParentedSpawner customEntity)
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

	public final ApplyableEntitySpawner[] getOverwriteEntities()
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
