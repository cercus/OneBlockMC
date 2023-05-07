package fr.cercusmc.oneblockmc.islands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;

import fr.cercusmc.oneblockmc.Main;

public class IslandConfig {

	private String overworld;
	private Biome biomeDefault;
	private int positionY;
	private int radiusMin;
	private int radiusMax;
	private int spaceBetweenIsland;
	private Location spawn;

	public IslandConfig() {
		final FileConfiguration config = Main.getInstance().getConfig();
		this.overworld = config.getString("config.overworld");
		this.biomeDefault = Biome.valueOf(config.getString("config.biome_default"));
		this.positionY = config.getInt("config.positionY");
		this.radiusMin = config.getInt("config.radiusMin");
		this.radiusMax = config.getInt("config.radiusMax");
		this.spaceBetweenIsland = config.getInt("config.space_between_island");
		this.spawn = new Location(Bukkit.getWorld(config.getString("config.spawn.world")),
				config.getInt("config.spawn.x"), config.getInt("config.spawn.y"), config.getInt("config.spawn.z"));

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
	
	public Location getSpawn() {
		return spawn;
	}

}
