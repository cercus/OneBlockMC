package fr.cercusmc.oneblockmc.islands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.FileCustom;
import fr.cercusmc.oneblockmc.utils.Position;

public class ToolsIsland {
	
	private ToolsIsland() {}
	
	public static Island createIsland(UUID uuid) {
		
		int nbIsland = Main.getIslandsFile().getInt("nbIslands");
		Position islandPosition = Position.findNext(nbIsland);
		Location locIsland = islandPosition.toLocation(Main.getIslandConfig().getOverworld());
		Island newIsland = new Island();
		newIsland.setBans(new ArrayList<>());
		newIsland.setMembers(Arrays.asList(uuid.toString()));
		newIsland.setBiome(Biome.PLAINS);
		newIsland.setOwner(uuid);
		newIsland.setIslandCustomName("");
		
		Locations loc = new Locations();
		loc.setCenterIsland(locIsland);
		loc.setSpawnIsland(locIsland.add(0, 1, 0));
		newIsland.setLocations(loc);
		IslandStats stat = new IslandStats();
		stat.setLevel(0);
		stat.setNbBlock(0);
		stat.setPhase(1);
		stat.setRadiusIsland(Main.getIslandConfig().getRadiusMin());
		newIsland.setIslandStats(stat);
		
		locIsland.getBlock().setType(Material.GRASS_BLOCK);
		locIsland.clone().add(0, -1, 0).getBlock().setType(Material.BEDROCK);
		
		updateIslandInFile(newIsland);
		Bukkit.getPlayer(uuid).teleport(Position.getCenterOfBlock(locIsland));
		return newIsland;
	}
	
	public static Island deleteIsland(UUID uuid) {
		return null;
	}
	
	public static Biome changeBiomeOfIsland(UUID uuid, Biome newBiome) {
		return newBiome;
	}
	
	public static Island updateIslandInFile(Island island) {
		FileCustom c = Main.getIslandsFile();
		ConfigurationSection section = null;
		String beginPath = "islands.";
		if(c.contains(beginPath+island.getOwner().toString())) {
			section = c.getConfigurationSection(beginPath+island.getOwner().toString());
		} else {
			section = c.createSection(beginPath+island.getOwner().toString());
		}
		
		section.set("bans", island.getBans());
		section.set("members", island.getMembers());
		section.set("biome", island.getBiome().name());
		section.set("islandCustomName", island.getIslandCustomName());
		Locations loc = island.getLocations();
		section.set("locations.centerIsland", loc.getCenterIsland());
		section.set("locations.homeIsland", loc.getHomeIsland());
		section.set("locations.spawnIsland", loc.getSpawnIsland());
		section.set("locations.warpIsland", loc.getWarpIsland());
		IslandStats stats = island.getIslandStats();
		section.set("islandStats.level", stats.getLevel());
		section.set("islandStats.nbBlock", stats.getNbBlock());
		section.set("islandStats.phase", stats.getPhase());
		section.set("islandStats.radiusIsland", stats.getRadiusIsland());
		c.save();
		if(Main.getIslands().containsKey(island.getOwner()))
			Main.getIslands().replace(island.getOwner(), island);
		else
			Main.getIslands().put(island.getOwner(), island);
		return island;
	}
	
	public static Map<UUID, Island> getAllIslands() {
		Map<UUID, Island> islands = new HashMap<>();
		FileCustom c = Main.getIslandsFile();
		for(String i : c.getConfigurationSection("islands").getKeys(false)) {
			Island island = new Island();
			island.setOwner(UUID.fromString(i));
			String ownerPath = "islands."+i;
			island.setBans(c.getStringList(ownerPath+".bans"));
			island.setMembers(c.getStringList(ownerPath+".members"));
			island.setBiome(Biome.valueOf(c.getString(ownerPath+".biome")));
			island.setIslandCustomName(c.getString(ownerPath+".islandCustomName"));
			
			Locations loc = new Locations();
			loc.setCenterIsland(c.getLocation(ownerPath+".locations.centerIsland"));
			loc.setHomeIsland(c.getLocation(ownerPath+".locations.homeIsland"));
			loc.setSpawnIsland(c.getLocation(ownerPath+".locations.spawnIsland"));
			loc.setWarpIsland(c.getLocation(ownerPath+".locations.warpIsland"));
			island.setLocations(loc);
			
			IslandStats stats = new IslandStats();
			stats.setLevel(c.getDouble(ownerPath+".islandStats.level"));
			stats.setNbBlock(c.getInt(ownerPath+".islandStats.nbBlock"));
			stats.setPhase(c.getInt(ownerPath+".islandStats.phase"));
			stats.setRadiusIsland(c.getInt(ownerPath+".islandStats.radiusIsland"));
			
			island.setIslandStats(stats);
			
			islands.put(island.getOwner(), island);
			
		}
		
		return islands;
	}
	
	/**
	 * Get ths island of player
	 * @param uuid - UUID of player
	 * @return Island of player
	 */
	public static Island getIslandOfPlayer(UUID uuid) {
		
		Optional<Island> island = getAllIslands().values().stream().filter(k -> k.getOwner().equals(uuid) || k.getMembers().contains(uuid.toString())).findFirst();
		
		return island.isPresent() ? island.get() : null;
	}

}
