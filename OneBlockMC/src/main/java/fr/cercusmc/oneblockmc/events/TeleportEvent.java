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
		Optional<Island> is = ToolsIsland.getIslandByLocation(locTo);
		if(is.isPresent() && is.get().playerIsBanned(e.getPlayer().getUniqueId().toString())) {
			MessageUtil.sendMessage(e.getPlayer(), Main.getFiles().get(Constantes.MESSAGES).getString("island.player_banned"));
			e.setCancelled(true);
		}
	}

}
