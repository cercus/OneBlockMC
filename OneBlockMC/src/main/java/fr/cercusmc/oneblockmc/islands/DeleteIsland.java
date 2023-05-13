package fr.cercusmc.oneblockmc.islands;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;

import fr.cercusmc.oneblockmc.Main;

public class DeleteIsland {
	
	private DeleteIsland() {}
	
	public static CompletableFuture<Void> deleteIsland(Island is) {

		return CompletableFuture.runAsync(() -> {
			List<CompletableFuture<Void>> completableFutures = Arrays.asList(deleteIsland(is, Main.getOverworld())

			);
			completableFutures.forEach(CompletableFuture::join);

		});

	}

	private static CompletableFuture<Void> deleteIsland(Island is, World world) {
		CompletableFuture<Void> completableFuture = new CompletableFuture<>();
		if (world == null)
			completableFuture.complete(null);
		else
			Bukkit.getScheduler().runTask(Main.getInstance(),
					() -> deleteIsland(is, world, world.getMaxHeight(), completableFuture, 0));
		return completableFuture;
	}

	private static void deleteIsland(Island is, World world, int y, CompletableFuture<Void> completableFuture,
			int delai) {
		Location center = is.getLocations().getCenterIsland();
		for (int x = center.getBlockX() - is.getIslandStats().getRadiusIsland(); x <= center.getBlockX() + is.getIslandStats().getRadiusIsland(); x++) {
			for (int z = center.getBlockX() - is.getIslandStats().getRadiusIsland(); z <= center.getBlockX()
					+ is.getIslandStats().getRadiusIsland(); z++) {
				Block b = world.getBlockAt(x, y, z);

				if (b.getType().equals(Material.AIR))
					continue;
				if (b.getState() instanceof InventoryHolder inventoryHolder)
					inventoryHolder.getInventory().clear();
				b.setType(Material.AIR, false);

			}
		}
		if (y <= world.getMinHeight())
			completableFuture.complete(null);
		else
			deleteIsland(is, world, y - 1, completableFuture, delai);

	}

}
