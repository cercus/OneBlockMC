package fr.cercusmc.oneblockmc.islands;

import java.util.List;
import java.util.UUID;

import org.bukkit.block.Biome;

public class Island {
	
	private UUID owner;
	private List<String> members;
	private List<String> bans;
	private String islandCustomName;
	private Locations locations;
	private Biome biome;
	private IslandStats islandStats;
	
	public Island() {}
	
	
	public Island(UUID owner, List<String> members, List<String> bans, String islandCustomName,
			Locations locations, Biome biome, IslandStats islandStats) {
		this.owner = owner;
		this.members = members;
		this.bans = bans;
		this.islandCustomName = islandCustomName;
		this.islandStats = islandStats;
		this.locations = locations;
		this.biome = biome;
	}
	
	public Island addBan(String uuid) {
		this.bans.add(uuid);
		return this;
	}
	
	public boolean playerIsBanned(String uuid) {
		return this.bans.contains(uuid);
	}
	
	public Island removeBan(String uuid) {
		this.bans.remove(uuid);
		return this;
	}


	public UUID getOwner() {
		return owner;
	}


	public void setOwner(UUID owner) {
		this.owner = owner;
	}


	public List<String> getMembers() {
		return members;
	}


	public void setMembers(List<String> members) {
		this.members = members;
	}


	public List<String> getBans() {
		return bans;
	}


	public void setBans(List<String> bans) {
		this.bans = bans;
	}


	public String getIslandCustomName() {
		return islandCustomName;
	}


	public void setIslandCustomName(String islandCustomName) {
		this.islandCustomName = islandCustomName;
	}


	public Locations getLocations() {
		return locations;
	}


	public void setLocations(Locations locations) {
		this.locations = locations;
	}


	public Biome getBiome() {
		return biome;
	}


	public void setBiome(Biome biome) {
		this.biome = biome;
	}


	public IslandStats getIslandStats() {
		return islandStats;
	}


	public void setIslandStats(IslandStats islandStats) {
		this.islandStats = islandStats;
	}
	
	
	
	
	
	

}
