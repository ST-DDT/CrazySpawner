package de.st_ddt.crazyutil.tests;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import com.avaje.ebean.config.ServerConfig;

@SuppressWarnings("deprecation")
public class DummyServer implements Server
{

	private final ItemFactory itemFactory = CraftItemFactory.instance();

	@Override
	public void sendPluginMessage(final Plugin paramPlugin, final String paramString, final byte[] paramArrayOfByte)
	{
	}

	@Override
	public Set<String> getListeningPluginChannels()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "DummyServer";
	}

	@Override
	public String getVersion()
	{
		return "1.7.2";
	}

	@Override
	public String getBukkitVersion()
	{
		return "1.7-R1";
	}

	@Override
	public Player[] getOnlinePlayers()
	{
		return null;
	}

	@Override
	public int getMaxPlayers()
	{
		return 0;
	}

	@Override
	public int getPort()
	{
		return 25565;
	}

	@Override
	public int getViewDistance()
	{
		return 0;
	}

	@Override
	public String getIp()
	{
		return "localhost";
	}

	@Override
	public String getServerName()
	{
		return "DummyServer";
	}

	@Override
	public String getServerId()
	{
		return null;
	}

	@Override
	public String getWorldType()
	{
		return null;
	}

	@Override
	public boolean getGenerateStructures()
	{
		return false;
	}

	@Override
	public boolean getAllowEnd()
	{
		return false;
	}

	@Override
	public boolean getAllowNether()
	{
		return false;
	}

	@Override
	public boolean hasWhitelist()
	{
		return false;
	}

	@Override
	public void setWhitelist(final boolean paramBoolean)
	{
	}

	@Override
	public Set<OfflinePlayer> getWhitelistedPlayers()
	{
		return null;
	}

	@Override
	public void reloadWhitelist()
	{
	}

	@Override
	public int broadcastMessage(final String paramString)
	{
		return 0;
	}

	@Override
	public String getUpdateFolder()
	{
		return null;
	}

	@Override
	public File getUpdateFolderFile()
	{
		return null;
	}

	@Override
	public long getConnectionThrottle()
	{
		return 0;
	}

	@Override
	public int getTicksPerAnimalSpawns()
	{
		return 0;
	}

	@Override
	public int getTicksPerMonsterSpawns()
	{
		return 0;
	}

	@Override
	public Player getPlayer(final String paramString)
	{
		return null;
	}

	@Override
	public Player getPlayerExact(final String paramString)
	{
		return null;
	}

	@Override
	public List<Player> matchPlayer(final String paramString)
	{
		return null;
	}

	@Override
	public PluginManager getPluginManager()
	{
		return null;
	}

	@Override
	public BukkitScheduler getScheduler()
	{
		return null;
	}

	@Override
	public ServicesManager getServicesManager()
	{
		return null;
	}

	@Override
	public List<World> getWorlds()
	{
		return null;
	}

	@Override
	public World createWorld(final WorldCreator paramWorldCreator)
	{
		return null;
	}

	@Override
	public boolean unloadWorld(final String paramString, final boolean paramBoolean)
	{
		return false;
	}

	@Override
	public boolean unloadWorld(final World paramWorld, final boolean paramBoolean)
	{
		return false;
	}

	@Override
	public World getWorld(final String paramString)
	{
		return null;
	}

	@Override
	public World getWorld(final UUID paramUUID)
	{
		return null;
	}

	@Deprecated
	@Override
	public MapView getMap(final short paramShort)
	{
		return null;
	}

	@Override
	public MapView createMap(final World paramWorld)
	{
		return null;
	}

	@Override
	public void reload()
	{
	}

	@Override
	public Logger getLogger()
	{
		return Logger.getLogger(DummyServer.class.getSimpleName());
	}

	@Override
	public PluginCommand getPluginCommand(final String paramString)
	{
		return null;
	}

	@Override
	public void savePlayers()
	{
	}

	@Override
	public boolean dispatchCommand(final CommandSender paramCommandSender, final String paramString) throws CommandException
	{
		return false;
	}

	@Override
	public void configureDbConfig(final ServerConfig paramServerConfig)
	{
	}

	@Override
	public boolean addRecipe(final Recipe paramRecipe)
	{
		return false;
	}

	@Override
	public List<Recipe> getRecipesFor(final ItemStack paramItemStack)
	{
		return null;
	}

	@Override
	public Iterator<Recipe> recipeIterator()
	{
		return null;
	}

	@Override
	public void clearRecipes()
	{
	}

	@Override
	public void resetRecipes()
	{
	}

	@Override
	public Map<String, String[]> getCommandAliases()
	{
		return null;
	}

	@Override
	public int getSpawnRadius()
	{
		return 0;
	}

	@Override
	public void setSpawnRadius(final int paramInt)
	{
	}

	@Override
	public boolean getOnlineMode()
	{
		return false;
	}

	@Override
	public boolean getAllowFlight()
	{
		return false;
	}

	@Override
	public boolean isHardcore()
	{
		return false;
	}

	@Override
	public boolean useExactLoginLocation()
	{
		return false;
	}

	@Override
	public void shutdown()
	{
	}

	@Override
	public int broadcast(final String paramString1, final String paramString2)
	{
		return 0;
	}

	@Override
	public OfflinePlayer getOfflinePlayer(final String paramString)
	{
		return null;
	}

	@Override
	public Set<String> getIPBans()
	{
		return null;
	}

	@Override
	public void banIP(final String paramString)
	{
	}

	@Override
	public void unbanIP(final String paramString)
	{
	}

	@Override
	public Set<OfflinePlayer> getBannedPlayers()
	{
		return null;
	}

	@Override
	public BanList getBanList(final Type paramType)
	{
		return null;
	}

	@Override
	public Set<OfflinePlayer> getOperators()
	{
		return null;
	}

	@Override
	public GameMode getDefaultGameMode()
	{
		return null;
	}

	@Override
	public void setDefaultGameMode(final GameMode paramGameMode)
	{
	}

	@Override
	public ConsoleCommandSender getConsoleSender()
	{
		return null;
	}

	@Override
	public File getWorldContainer()
	{
		return null;
	}

	@Override
	public OfflinePlayer[] getOfflinePlayers()
	{
		return null;
	}

	@Override
	public Messenger getMessenger()
	{
		return null;
	}

	@Override
	public HelpMap getHelpMap()
	{
		return null;
	}

	@Override
	public Inventory createInventory(final InventoryHolder paramInventoryHolder, final InventoryType paramInventoryType)
	{
		return null;
	}

	@Override
	public Inventory createInventory(final InventoryHolder paramInventoryHolder, final int paramInt)
	{
		return null;
	}

	@Override
	public Inventory createInventory(final InventoryHolder paramInventoryHolder, final int paramInt, final String paramString)
	{
		return null;
	}

	@Override
	public int getMonsterSpawnLimit()
	{
		return 0;
	}

	@Override
	public int getAnimalSpawnLimit()
	{
		return 0;
	}

	@Override
	public int getWaterAnimalSpawnLimit()
	{
		return 0;
	}

	@Override
	public int getAmbientSpawnLimit()
	{
		return 0;
	}

	@Override
	public boolean isPrimaryThread()
	{
		return false;
	}

	@Override
	public String getMotd()
	{
		return null;
	}

	@Override
	public String getShutdownMessage()
	{
		return null;
	}

	@Override
	public WarningState getWarningState()
	{
		return null;
	}

	@Override
	public ItemFactory getItemFactory()
	{
		return itemFactory;
	}

	@Override
	public ScoreboardManager getScoreboardManager()
	{
		return null;
	}

	@Override
	public CachedServerIcon getServerIcon()
	{
		return null;
	}

	@Override
	public CachedServerIcon loadServerIcon(final File paramFile) throws IllegalArgumentException, Exception
	{
		return null;
	}

	@Override
	public CachedServerIcon loadServerIcon(final BufferedImage paramBufferedImage) throws IllegalArgumentException, Exception
	{
		return null;
	}

	@Override
	public void setIdleTimeout(final int paramInt)
	{
	}

	@Override
	public int getIdleTimeout()
	{
		return 0;
	}

	@Deprecated
	@Override
	public UnsafeValues getUnsafe()
	{
		return null;
	}
}
