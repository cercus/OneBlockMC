package fr.cercusmc.oneblockmc.events;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.phases.Phase;
import fr.cercusmc.oneblockmc.phases.ToolsPhase;
import fr.cercusmc.oneblockmc.utils.Bar;
import fr.cercusmc.oneblockmc.utils.MessageUtil;

public class JoinEvent implements Listener {
	
	private static final String PLAYERS2 = "players";
	private static final String DELETE_PLAYER_WAITING = "deletePlayerWaiting";

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Island is = ToolsIsland.getIslandOfPlayer(e.getPlayer().getUniqueId());
		
		if(is != null) {
			
			Optional<Phase> phaseCurrent = ToolsPhase.getPhase(is.getIslandStats().getPhase());
			Optional<Phase> nextPhase = ToolsPhase.getPhase(is.getIslandStats().getPhase()+1);
			updateBar(phaseCurrent, nextPhase, e.getPlayer(), is);

			
		}
		
		if(Main.getFiles().get(DELETE_PLAYER_WAITING).getStringList(PLAYERS2).contains(e.getPlayer().getUniqueId().toString())) {
			e.getPlayer().teleport(Main.getIslandConfig().getSpawn());
			Set<String> players = Main.getFiles().get(DELETE_PLAYER_WAITING).getStringList(PLAYERS2).stream().collect(Collectors.toSet());
			players.remove(e.getPlayer().getUniqueId().toString());
			Main.getFiles().get(DELETE_PLAYER_WAITING).set(PLAYERS2, players);
			MessageUtil.sendMessage(Bukkit.getPlayer(e.getPlayer().getUniqueId()), Main.getFiles().get("messages").getString("island.message_member_deleted_island"));
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Island is = ToolsIsland.getIslandOfPlayer(e.getPlayer().getUniqueId());
		
		if(is != null) {
			
			if(Main.getBars().containsKey(e.getPlayer().getUniqueId())) {
				Bar bar = Main.getBars().get(e.getPlayer().getUniqueId());
				bar.setVisible(false);
				bar.removePlayer(e.getPlayer());
				Main.getBars().remove(e.getPlayer().getUniqueId());
			}

			
		}
	}
	
	public static void updateBar(Optional<Phase> phaseCurrent, Optional<Phase> nextPhase, Player p, Island is) {
		if(phaseCurrent.isPresent()) {
			Bar bar = Main.getBars().get(p.getUniqueId());
			
			if(nextPhase.isEmpty()) {
				
				if(null == bar) {
				
					bar = new Bar(BarColor.BLUE, BarStyle.SOLID, phaseCurrent.get().name());
					bar.setProgress(1.0);
				} else {
					bar.setProgress(1.0);
				}
			} else {
				if(null == bar) {
					bar = new Bar(BarColor.BLUE, BarStyle.SOLID, phaseCurrent.get().name() + " >> " + nextPhase.get().name());
					bar.setProgress((is.getIslandStats().getNbBlock()*1f-phaseCurrent.get().blockToReach())/(nextPhase.get().blockToReach()-phaseCurrent.get().blockToReach()*1f));
			
				} else {
					bar.setProgress((is.getIslandStats().getNbBlock()*1f-phaseCurrent.get().blockToReach())/(nextPhase.get().blockToReach()-phaseCurrent.get().blockToReach()*1f));
				}
			}
			if(!bar.getPlayers().contains(p))
				bar.addPlayer(p);
			bar.setVisible(true);
			if(Main.getBars().containsKey(p.getUniqueId())) {
				Main.getBars().replace(p.getUniqueId(), bar);
			} else {
				Main.getBars().put(p.getUniqueId(), bar);
			}
		}
	}

}
