package fr.cercusmc.oneblockmc.islands;

import org.bukkit.block.Biome;

import fr.cercusmc.oneblockmc.Main;

public class IslandConfig {
	
	private String overworld;
	private Biome biomeDefault;
	private int positionY;
	private int radiusMin;
	private int radiusMax;
	private int spaceBetweenIsland;
	
	public IslandConfig() {
		this.overworld = Main.getInstance().getConfig().getString("config.overworld");
		this.biomeDefault = Biome.valueOf(Main.getInstance().getConfig().getString("config.biome_default"));
		this.positionY = Main.getInstance().getConfig().getInt("config.positionY");
		this.radiusMin = Main.getInstance().getConfig().getInt("config.radiusMin");
		this.radiusMax = Main.getInstance().getConfig().getInt("config.radiusMax");
		this.spaceBetweenIsland = Main.getInstance().getConfig().getInt("config.space_between_island");
		
		
	}
	
	public Biome getBiomeDefault() {
		return biomeDefault;
	}
	
	public int getPositionY() {
		return positionY;
	}
	
	public String getOverworld() {
		return overworld;
	}
	
	public int getRadiusMax() {
		return radiusMax;
	}
	
	public int getRadiusMin() {
		return radiusMin;
	}
	
	public int getSpaceBetweenIsland() {
		return spaceBetweenIsland;
	}

}
