package fr.cercusmc.oneblockmc.generators;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import fr.cercusmc.oneblockmc.Main;

public class VoidGenerator extends ChunkGenerator {
	
	private byte[][] blockSections;
	
	@SuppressWarnings("deprecation")
	@Override
	public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
		ChunkData chunkData = super.createChunkData(world);
        for(int x1 = 0; x1 < 16; x1++) {
            for(int z1 = 0; z1 < 16; z1++) {
                biome.setBiome(x, z, Main.getIslandConfig().getBiomeDefault());
                
            }
        }
		return chunkData;
	}
	
	@SuppressWarnings("deprecation")
	public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomeGrid) {
        if (blockSections == null) {
            blockSections = new byte[world.getMaxHeight() / 16][];
        }
        return blockSections;
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.emptyList();
    }

}
