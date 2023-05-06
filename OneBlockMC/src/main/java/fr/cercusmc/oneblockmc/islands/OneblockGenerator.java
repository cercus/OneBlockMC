package fr.cercusmc.oneblockmc.islands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class OneblockGenerator extends ChunkGenerator {
	
	@Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return new ArrayList<>();
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biome) {
        return createChunkData(world);
    }

}
