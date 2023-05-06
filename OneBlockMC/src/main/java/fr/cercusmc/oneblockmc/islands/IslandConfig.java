package fr.cercusmc.oneblockmc.islands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;

import fr.cercusmc.oneblockmc.Main;

public class IslandConfig {
	
	private World overworld;
	private Biome biomeDefault;
	private int positionY;
	private int radiusMin;
	private int radiusMax;
	
	public IslandConfig() {
		this.overworld = Bukkit.getWorld(Main.getInstance().getConfig().getString("config.overworld"));
		this.biomeDefault = Biome.valueOf(Main.getInstance().getConfig().getString("config.biomeDefault"));
		this.positionY = Main.getInstance().getConfig().getInt("config.positionY");
		this.radiusMin = Main.getInstance().getConfig().getInt("config.radiusMin");
		this.radiusMax = Main.getInstance().getConfig().getInt("config.radiusMax");
		
		
	}
	
	public Biome getBiomeDefault() {
		return biomeDefault;
	}
	
	public int getPositionY() {
		return positionY;
	}
	
	public World getOverworld() {
		return overworld;
	}
	
	public int getRadiusMax() {
		return radiusMax;
	}
	
	public int getRadiusMin() {
		return radiusMin;
	}

}
