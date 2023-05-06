package fr.cercusmc.oneblockmc.islands;

public class IslandStats {
	
	private int nbBlock;
	private int radiusIsland;
	private double level;
	private int phase;
	
	
	public IslandStats(int nbBlock, int radiusIsland, double level, int phase) {
		this.nbBlock = nbBlock;
		this.radiusIsland = radiusIsland;
		this.level = level;
		this.phase = phase;
	}


	public IslandStats() {
	}


	public int getNbBlock() {
		return nbBlock;
	}


	public void setNbBlock(int nbBlock) {
		this.nbBlock = nbBlock;
	}


	public int getRadiusIsland() {
		return radiusIsland;
	}


	public void setRadiusIsland(int radiusIsland) {
		this.radiusIsland = radiusIsland;
	}


	public double getLevel() {
		return level;
	}


	public void setLevel(double level) {
		this.level = level;
	}


	public int getPhase() {
		return phase;
	}


	public void setPhase(int phase) {
		this.phase = phase;
	}
	
	
	
	

}
