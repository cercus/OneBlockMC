package fr.cercusmc.oneblockmc.islands;

import org.bukkit.Location;

public class Locations {
	
	private Location centerIsland;
	private Location warpIsland;
	private Location homeIsland;
	private Location spawnIsland;
	
	public Locations(Location centerIsland, Location warpIsland, Location homeIsland, Location spawnIsland) {
		this.centerIsland = centerIsland;
		this.warpIsland = warpIsland;
		this.homeIsland = homeIsland;
		this.spawnIsland = spawnIsland;
	}

	public Locations() {
	}

	public Location getCenterIsland() {
		return centerIsland;
	}

	public void setCenterIsland(Location centerIsland) {
		this.centerIsland = centerIsland;
	}

	public Location getWarpIsland() {
		return warpIsland;
	}

	public void setWarpIsland(Location warpIsland) {
		this.warpIsland = warpIsland;
	}

	public Location getHomeIsland() {
		return homeIsland;
	}

	public void setHomeIsland(Location homeIsland) {
		this.homeIsland = homeIsland;
	}

	public Location getSpawnIsland() {
		return spawnIsland;
	}

	public void setSpawnIsland(Location spawnIsland) {
		this.spawnIsland = spawnIsland;
	}
	
	
	
	

}
