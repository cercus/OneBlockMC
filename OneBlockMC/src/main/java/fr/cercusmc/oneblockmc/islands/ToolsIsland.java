package fr.cercusmc.oneblockmc.islands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.BoundingBox;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.FileCustom;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.Position;
import fr.cercusmc.oneblockmc.utils.enums.PlaceHolderType;

public class ToolsIsland {

	private ToolsIsland() {
	}

	/**
	 * Create one island
	 * 
	 * @param uuid - UUID of player
	 * @return The island created
	 */
	public static Island createIsland(UUID uuid) {

		MessageUtil.sendMessage(Bukkit.getPlayer(uuid),
				Main.getFiles().get(Constantes.MESSAGES).getString("island.creating_island"));
		FileCustom c = Main.getFiles().get(Constantes.ISLANDS);
		int nbIsland = c.getInt("nbIslands");
		Position islandPosition = Position.findNext(nbIsland);
		Location locIsland = islandPosition.toLocation(Main.getOverworld());
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
		c.set("nbIslands", nbIsland + 1);
		c.save();
		changeBiomeOfIsland(uuid, Main.getIslandConfig().getBiomeDefault());
		Bukkit.getPlayer(uuid).teleport(Position.getCenterOfBlock(locIsland));

		EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
		map.put(PlaceHolderType.LOC_X, locIsland.getX() + "");
		map.put(PlaceHolderType.LOC_Y, locIsland.getY() + "");
		map.put(PlaceHolderType.LOC_Z, locIsland.getZ() + "");
		MessageUtil.sendMessage(Bukkit.getPlayer(uuid),
				Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_create_island"), map);

		return newIsland;
	}

	/**
	 * Delete the island of player
	 * 
	 * @param uuid - UUID of player
	 * @return
	 */
	public static Island deleteIsland(Island is, UUID uuid) {
		if (is == null)
			return null;
		DeleteIsland.deleteIsland(is);
		Main.getFiles().get(Constantes.ISLANDS).set(Constantes.ISLANDS + "." + uuid.toString(), null);
		MessageUtil.sendMessage(uuid,
				Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_delete_island"));
		updateIslandVariable(is);
		Bukkit.getPlayer(uuid).teleport(Main.getIslandConfig().getSpawn());
		return is;
	}

	public static void calcLevel(UUID uuid) {
		Island is = getIslandOfPlayer(uuid);
		if (is == null)
			return;

		CalcIsland.calcIsland(is, Bukkit.getPlayer(uuid));
	}

	public static Biome changeBiomeOfIsland(UUID uuid, Biome newBiome) {

		Island is = getIslandOfPlayer(uuid);
		if (is == null)
			return null;
		ChangeBiome.changeBiome(is, Bukkit.getPlayer(uuid), newBiome);
		is.setBiome(newBiome);
		updateIslandVariable(is);
		updateIslandInFile(is);
		return newBiome;
	}

	public static Island updateIslandInFile(Island island) {
		FileCustom c = Main.getFiles().get(Constantes.ISLANDS);
		ConfigurationSection section = null;
		String beginPath = "islands.";
		if (c.contains(beginPath + island.getOwner().toString())) {
			section = c.getConfigurationSection(beginPath + island.getOwner().toString());
		} else {
			section = c.createSection(beginPath + island.getOwner().toString());
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
		updateIslandVariable(island);
		return island;
	}

	public static void updateIslandVariable(Island island) {
		if (Main.getIslands().containsKey(island.getOwner()))
			Main.getIslands().replace(island.getOwner(), island);
		else
			Main.getIslands().put(island.getOwner(), island);
	}

	public static Map<UUID, Island> getAllIslands() {
		Map<UUID, Island> islands = new HashMap<>();
		FileCustom c = Main.getFiles().get(Constantes.ISLANDS);
		if (c.getConfigurationSection(Constantes.ISLANDS) == null)
			return islands;
		for (String i : c.getConfigurationSection(Constantes.ISLANDS).getKeys(false)) {
			Island island = new Island();
			island.setOwner(UUID.fromString(i));
			String ownerPath = "islands." + i;
			island.setBans(c.getStringList(ownerPath + ".bans"));
			island.setMembers(c.getStringList(ownerPath + ".members"));
			island.setBiome(Biome.valueOf(c.getString(ownerPath + ".biome")));
			island.setIslandCustomName(c.getString(ownerPath + ".islandCustomName"));

			Locations loc = new Locations();
			loc.setCenterIsland(c.getLocation(ownerPath + ".locations.centerIsland"));
			loc.setHomeIsland(c.getLocation(ownerPath + ".locations.homeIsland"));
			loc.setSpawnIsland(c.getLocation(ownerPath + ".locations.spawnIsland"));
			loc.setWarpIsland(c.getLocation(ownerPath + ".locations.warpIsland"));
			island.setLocations(loc);

			IslandStats stats = new IslandStats();
			stats.setLevel(c.getDouble(ownerPath + ".islandStats.level"));
			stats.setNbBlock(c.getInt(ownerPath + ".islandStats.nbBlock"));
			stats.setPhase(c.getInt(ownerPath + ".islandStats.phase"));
			stats.setRadiusIsland(c.getInt(ownerPath + ".islandStats.radiusIsland"));

			island.setIslandStats(stats);

			islands.put(island.getOwner(), island);

		}

		return islands;
	}

	/**
	 * Get ths island of player
	 * 
	 * @param uuid - UUID of player
	 * @return Island of player
	 */
	public static Island getIslandOfPlayer(UUID uuid) {

		Optional<Island> island = getAllIslands().values().stream()
				.filter(k -> k.getOwner().equals(uuid) || k.getMembers().contains(uuid.toString())).findFirst();

		return island.isPresent() ? island.get() : null;
	}

	public static World createWorld(Environment environnement, String name) {
		WorldCreator wc = new WorldCreator(name);
		wc.environment(environnement);
		wc.generateStructures(false);
		wc.generator(Main.getInstance().getDefaultWorldGenerator(name, null));
		if (Bukkit.getWorld(name) == null) {
			Bukkit.getServer().createWorld(wc);
		}
		return Bukkit.getWorld(name);
	}
	
	public static boolean locIsInIsland(Island is, Location loc) {
		Location locCenter = is.getLocations().getCenterIsland();
		BoundingBox b = new BoundingBox(locCenter.getBlockX()*1f - Main.getIslandConfig().getRadiusMax(),
				locCenter.getWorld().getMinHeight(), locCenter.getBlockZ()*1f - Main.getIslandConfig().getRadiusMax(),
				locCenter.getBlockX()*1f + Main.getIslandConfig().getRadiusMax(), loc.getWorld().getMaxHeight(),
				locCenter.getBlockZ()*1f + Main.getIslandConfig().getRadiusMax());
		
		return b.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public static Optional<Island> getIslandByLocation(Location loc) {
		
		return Main.getIslands().values().stream().filter(k -> locIsInIsland(k, loc)).findFirst();


	}

}
