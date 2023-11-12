package fr.cercusmc.oneblockmc.events;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;

public class PlaceBlockEvent implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		
		Island is = ToolsIsland.getIslandOfPlayer(e.getPlayer().getUniqueId());
		
		if(is == null && e.getPlayer().getWorld().getName().equals(Main.getOverworld().getName())) {
			MessageUtil.sendMessage(e.getPlayer(), Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
			e.setCancelled(true);
			return;
		} else if(is == null) {
			return;
		}
		
		if(!ToolsIsland.locIsInIslandAvailableBuild(is, e.getBlock().getLocation())) {
			e.getPlayer().sendMessage("Vous ne pouvez pas construire en dehors de votre limite de build");
			e.setCancelled(true);
			return;
		}
		
		Optional<Island> isOpt = ToolsIsland.getIslandByLocation(e.getBlock().getLocation());
		
		if(isOpt.isEmpty()) return;
		
		if(!isOpt.get().getMembers().contains(e.getPlayer().getUniqueId().toString())) {
			e.getPlayer().sendMessage("Pas membre de l'ile");
			e.setCancelled(true);
		}
	}

}
