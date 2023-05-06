package fr.cercusmc.oneblockmc.islands;

import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.PlaceHolderType;

public class ChangeBiome {
	
	private ChangeBiome() {}
	
	public static CompletableFuture<Void> changeBiome(Island is, Player p, Biome biome) {
		if(is == null || biome == null)
			return new CompletableFuture<>();
		return CompletableFuture.runAsync(() -> {
			CompletableFuture<Void> completableFuture = changeBiome(is, Main.getOverworld(), p, biome);
			completableFuture.join();
			EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
			map.put(PlaceHolderType.BIOME_NAME, biome.name());
			MessageUtil.sendMessage(p, Main.getFiles().get("messages").getString("island.successfull_change_biome"), map);
		});
	}

	private static CompletableFuture<Void> changeBiome(Island is, World world, Player p, Biome biome) {
		 CompletableFuture<Void> completableFuture = new CompletableFuture<>();
		 if(world == null)
			 completableFuture.complete(null);
		 else
			 Bukkit.getScheduler().runTask(Main.getInstance(), () -> changeBiome(is, world, world.getMaxHeight(), p, 0, completableFuture, biome));
		return completableFuture;
	}

	private static void changeBiome(Island is, World world, int maxHeight, Player p, int y,
			CompletableFuture<Void> completableFuture, Biome biome) {
		Location center = is.getLocations().getCenterIsland();
		for (int x = center.getBlockX() - is.getIslandStats().getRadiusIsland(); x <= center.getBlockX() + is.getIslandStats().getRadiusIsland(); x++) {
			for (int z = center.getBlockX() - is.getIslandStats().getRadiusIsland(); z <= center.getBlockX()
					+ is.getIslandStats().getRadiusIsland(); z++) {
				Block b = world.getBlockAt(x, y, z);
				b.setBiome(biome);

			}
		}
		if (y <= maxHeight)
			completableFuture.complete(null);
		else
			changeBiome(is, world, maxHeight, p, y-1, completableFuture, biome);
	}

}