package fr.cercusmc.oneblockmc.events;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.MessageUtil;

public class JoinEvent implements Listener {
	
	private static final String PLAYERS2 = "players";
	private static final String DELETE_PLAYER_WAITING = "deletePlayerWaiting";

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		if(Main.getFiles().get(DELETE_PLAYER_WAITING).getStringList(PLAYERS2).contains(e.getPlayer().getUniqueId().toString())) {
			e.getPlayer().teleport(Main.getIslandConfig().getSpawn());
			Set<String> players = Main.getFiles().get(DELETE_PLAYER_WAITING).getStringList(PLAYERS2).stream().collect(Collectors.toSet());
			players.remove(e.getPlayer().getUniqueId().toString());
			Main.getFiles().get(DELETE_PLAYER_WAITING).set(PLAYERS2, players);
			MessageUtil.sendMessage(Bukkit.getPlayer(e.getPlayer().getUniqueId()), Main.getFiles().get("messages").getString("island.message_member_deleted_island"));
		}
	}

}
