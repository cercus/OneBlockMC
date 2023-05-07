package fr.cercusmc.oneblockmc.islands;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.PlaceHolderType;

public class CalcIsland {
	
	private CalcIsland() {}
	
	public static CompletableFuture<Void> calcIsland(Island is, Player p) {
		return CompletableFuture.runAsync(() -> {
			List<CompletableFuture<Void>> completableFutures = Arrays.asList(calcIsland(is, Main.getOverworld(), p)
					);
			completableFutures.forEach(CompletableFuture::join);
					
		});
	}
	
	private static CompletableFuture<Void> calcIsland(Island is, World world, Player p) {
		 CompletableFuture<Void> completableFuture = new CompletableFuture<>();
		 if(world == null)
			 completableFuture.complete(null);
		 else
			 Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
				 Location center = is.getLocations().getCenterIsland();
					double level = 0;
					for(int x = center.getBlockX()-is.getIslandStats().getRadiusIsland(); x <= center.getBlockX()+is.getIslandStats().getRadiusIsland(); x++) {
						for(int z = center.getBlockX()-is.getIslandStats().getRadiusIsland(); z <= center.getBlockX()+is.getIslandStats().getRadiusIsland(); z++) {
							for(int y = world.getMinHeight(); y <= world.getMaxHeight(); y++) {
								Material b = world.getBlockAt(x, y, z).getType();
								level += Main.getFiles().get("levels").getDouble(b.name(), 0);
							}
						}
					}
					IslandStats isStats = is.getIslandStats();
					isStats.setLevel(level);
					is.setIslandStats(isStats);
					ToolsIsland.updateIslandInFile(is);
					ToolsIsland.updateIslandVariable(is);
					EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
					map.put(PlaceHolderType.LEVEL, level+"");
					MessageUtil.sendMessage(p, Main.getFiles().get("messages").getString("island.successfull_calc_level"), map);
			 });
		 return completableFuture;
	}

}
