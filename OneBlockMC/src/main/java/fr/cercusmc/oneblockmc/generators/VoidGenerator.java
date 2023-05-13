package fr.cercusmc.oneblockmc.generators;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import fr.cercusmc.oneblockmc.Main;

public class VoidGenerator extends ChunkGenerator {

	private byte[][] blockSections;

	@SuppressWarnings("deprecation")
	@Override
	public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
		ChunkData chunkData = super.createChunkData(world);
		setBiome(world, biome);
		return chunkData;
	}

	@SuppressWarnings("deprecation")
	private void setBiome(World world, BiomeGrid biomeGrid) {
		Biome biome = Main.getIslandConfig().getBiomeDefault();
		for (int x = 0; x < 16; x += 1) {
			for (int z = 0; z < 16; z += 1) {
				for (int y = world.getMinHeight(); y < world.getMaxHeight(); y += 4) {
					biomeGrid.setBiome(x, y, z, biome);
				}
			}
		}

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
