package fr.cercusmc.oneblockmc;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	@Override
	public void onEnable() {
		setinstance(this);
	}
	
	private static void setinstance(Main main) {
		instance = main;
		
	}

	public static Main getInstance() {
		return instance;
	}

}
