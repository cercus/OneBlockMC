package fr.cercusmc.oneblockmc;

import java.util.*;

import fr.cercusmc.oneblockmc.utils.items.CustomItem;
import fr.cercusmc.oneblockmc.utils.items.CustomItemHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.events.BreakBlockEvent;
import fr.cercusmc.oneblockmc.events.JoinEvent;
import fr.cercusmc.oneblockmc.events.MoveEvent;
import fr.cercusmc.oneblockmc.events.PlaceBlockEvent;
import fr.cercusmc.oneblockmc.events.TeleportEvent;
import fr.cercusmc.oneblockmc.generators.VoidGenerator;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.IslandConfig;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.phases.Phase;
import fr.cercusmc.oneblockmc.phases.ToolsPhase;
import fr.cercusmc.oneblockmc.settings.ToolSettings;
import fr.cercusmc.oneblockmc.utils.Bar;
import fr.cercusmc.oneblockmc.utils.files.FileCustom;
import fr.cercusmc.oneblockmc.utils.menus.MenuManager;

public class Main extends JavaPlugin {

	public static Map<String, CustomItem> customItemMap;
	private static Main instance;
	
	private static Map<String, FileCustom> files = new HashMap<>();
	
	private static Map<UUID, Island> islands;
	
	private static IslandConfig islandConfig;
	
	private ChunkGenerator chunkGenerator;
	
	private static List<Phase> phases;
	
	private static List<IslandInvite> invites = new ArrayList<>();
	
	private static Map<UUID, Bar> bars = new HashMap<>();
	
	private static List<FileCustom> filesSettings = new ArrayList<>();
	
	
	private static World overworld;

	private static Economy econ = null;

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return true;
	}
	public static void setIslandConfig() {
		islandConfig = new IslandConfig();
		
	}
	
	public static void setOverworld() {
		overworld = ToolsIsland.createWorld(Environment.NORMAL, islandConfig.getOverworld());
	}
	
	public static void setIslands() {
		islands = ToolsIsland.getAllIslands();
		phases = ToolsPhase.getAllPhases();
		filesSettings = ToolSettings.getAllFiles(instance);
	}

	public static Economy getEcon() {
		return econ;
	}

	@Override
	public void onEnable() {
		customItemMap = new HashMap<>();
		registerItems();
		if (!setupEconomy() ) {
			getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		setInstance(this);
		saveDefaultConfig();
		
		this.chunkGenerator = new VoidGenerator();
		
		
		setIslandConfig();
		
		setOverworld();
		loadFiles();
		
		setIslands();
		
		
		getCommand("ob").setExecutor(new OneBlockCommand());
		getCommand("ob").setTabCompleter(new OneBlockCommand());
		
		registerEvents();
		
		MenuManager.setup(getServer(), this);
				
	}

	private void registerItems(CustomItem... customItems) {
		Arrays.asList(customItems).forEach(ci-> customItemMap.put(ci.getId(), ci));
	}

	public static Map<String, CustomItem> getCustomItemMap() {
		return customItemMap;
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new JoinEvent(), instance);
		getServer().getPluginManager().registerEvents(new MoveEvent(), instance);
		getServer().getPluginManager().registerEvents(new TeleportEvent(), instance);
		getServer().getPluginManager().registerEvents(new BreakBlockEvent(), instance);
		getServer().getPluginManager().registerEvents(new PlaceBlockEvent(), instance);
		getServer().getPluginManager().registerEvents(new CustomItemHandler(), instance);
		
	}

	private static void loadFiles() {
		files.put("islands", new FileCustom(instance, "islands.yml"));
		files.put("messages", new FileCustom(instance, "messages.yml"));
		files.put("levels", new FileCustom(instance, "levels.yml"));
		files.put("deletePlayerWaiting", new FileCustom(instance, "deletePlayerWaiting.yml"));
		files.put("biomes", new FileCustom(instance, "biomes.yml"));
		files.put("phases", new FileCustom(instance, "phases.yml"));
		files.put("settings", new FileCustom(instance, "settingsConfig.yml"));
		files.put("upgrade", new FileCustom(instance, "upgrade.yml"));
		
	}

	private static void setInstance(Main main) {
		instance = main;
		
	}

	public static Main getInstance() {
		return instance;
	}
	
	
	public static Map<UUID, Island> getIslands() {
		return islands;
	}
	
	public static IslandConfig getIslandConfig() {
		return islandConfig;
	}
	
	public static World getOverworld() {
		return overworld;
	}

	public static Map<String, FileCustom> getFiles() {
		return files;
	}
	
	public static List<Phase> getPhases() {
		return phases;
	}

	
	@Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return this.chunkGenerator;
    }
	
	public static List<IslandInvite> getInvites() {
		return invites;
	}
	
	public static Map<UUID, Bar> getBars() {
		return bars;
	}
	
	public static List<FileCustom> getFilesSettings() {
		return filesSettings;
	}

}
