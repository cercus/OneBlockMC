package fr.cercusmc.oneblockmc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.events.BreakBlockEvent;
import fr.cercusmc.oneblockmc.events.JoinEvent;
import fr.cercusmc.oneblockmc.events.MoveEvent;
import fr.cercusmc.oneblockmc.events.TeleportEvent;
import fr.cercusmc.oneblockmc.generators.VoidGenerator;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.IslandConfig;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.phases.Phase;
import fr.cercusmc.oneblockmc.phases.ToolsPhase;
import fr.cercusmc.oneblockmc.utils.FileCustom;
import fr.cercusmc.oneblockmc.utils.menus.MenuManager;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private static Map<String, FileCustom> files = new HashMap<>();
	
	private static Map<UUID, Island> islands;
	
	private static IslandConfig islandConfig;
	
	private ChunkGenerator chunkGenerator;
	
	private static List<Phase> phases;
	
	
	private static World overworld;
	
	public static void setIslandConfig() {
		islandConfig = new IslandConfig();
	}
	
	public static void setOverworld() {
		overworld = ToolsIsland.createWorld(Environment.NORMAL, islandConfig.getOverworld());
	}
	
	public static void setIslands() {
		islands = ToolsIsland.getAllIslands();
		phases = ToolsPhase.getAllPhases();
	}
	
	@Override
	public void onEnable() {
		
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
	

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new JoinEvent(), instance);
		getServer().getPluginManager().registerEvents(new MoveEvent(), instance);
		getServer().getPluginManager().registerEvents(new TeleportEvent(), instance);
		getServer().getPluginManager().registerEvents(new BreakBlockEvent(), instance);
		
	}

	private static void loadFiles() {
		files.put("islands", new FileCustom(instance, "islands.yml"));
		files.put("messages", new FileCustom(instance, "messages.yml"));
		files.put("levels", new FileCustom(instance, "levels.yml"));
		files.put("deletePlayerWaiting", new FileCustom(instance, "deletePlayerWaiting.yml"));
		files.put("biomes", new FileCustom(instance, "biomes.yml"));
		files.put("phases", new FileCustom(instance, "phases.yml"));
		
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
}
