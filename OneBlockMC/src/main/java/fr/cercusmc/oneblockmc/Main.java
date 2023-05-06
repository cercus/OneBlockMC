package fr.cercusmc.oneblockmc;

import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.IslandConfig;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.FileCustom;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private static FileCustom islandsFile;
	
	private static Map<UUID, Island> islands;
	
	private static IslandConfig islandConfig;
	
	private static void setStaticVariable() {
		islands = ToolsIsland.getAllIslands();
		islandConfig = new IslandConfig();
	}
	
	@Override
	public void onEnable() {
		setInstance(this);
		
		setStaticVariable();
		
		loadFiles();
		
	}
	
	private static void loadFiles() {
		islandsFile = new FileCustom(instance, "islands.yml");
		
	}

	private static void setInstance(Main main) {
		instance = main;
		
	}

	public static Main getInstance() {
		return instance;
	}
	
	public static FileCustom getIslandsFile() {
		return islandsFile;
	}
	
	public static Map<UUID, Island> getIslands() {
		return islands;
	}
	
	public static IslandConfig getIslandConfig() {
		return islandConfig;
	}

}
