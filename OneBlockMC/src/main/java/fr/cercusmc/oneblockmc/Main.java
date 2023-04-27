package fr.cercusmc.oneblockmc;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private Main instance;
	
	@Override
	public void onEnable() {
		instance = this;
	}
	
	public Main getInstance() {
		return instance;
	}

}
