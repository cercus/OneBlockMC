package fr.cercusmc.oneblockmc.events;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;

public class TeleportEvent implements Listener {
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Location locTo = e.getTo();
		Optional<Island> isFrom = ToolsIsland.getIslandByLocation(e.getFrom());
		Optional<Island> isTo = ToolsIsland.getIslandByLocation(locTo);
		
		if(isFrom.isEmpty() && isTo.isEmpty()) return;
		
		if(isTo.isPresent() && isTo.get().playerIsBanned(e.getPlayer().getUniqueId().toString())) {
			MessageUtil.sendMessage(e.getPlayer(), Main.getFiles().get(Constantes.MESSAGES).getString("island.player_banned"));
			e.setCancelled(true);
			return;
		}
		
		if(isTo.isPresent() && isFrom.isPresent() && !isTo.get().getOwner().equals(isFrom.get().getOwner())) {
			MessageUtil.sendMessage(e.getPlayer(), "Vous quittez l'île de " + isFrom.get().getOwner());
			MessageUtil.sendMessage(e.getPlayer(), "Vous entrez sur l'île de " + isTo.get().getOwner());
			
		} else if(isTo.isPresent() && isFrom.isEmpty()) {
			MessageUtil.sendMessage(e.getPlayer(), "Vous entrez sur l'île de " + isTo.get().getOwner());
		} else if(isTo.isEmpty()) {
			MessageUtil.sendMessage(e.getPlayer(), "Vous quittez l'île de " + isFrom.get().getOwner());
		}
	}

}
