package fr.cercusmc.oneblockmc.events;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.IslandStats;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.phases.Phase;
import fr.cercusmc.oneblockmc.phases.RandomChoice;
import fr.cercusmc.oneblockmc.phases.ToolsPhase;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.enums.PlaceHolderType;

public class BreakBlockEvent implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		
		Island is = ToolsIsland.getIslandOfPlayer(e.getPlayer().getUniqueId());
		
		if(is == null) {
			MessageUtil.sendMessage(e.getPlayer(), Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
			return;
		}
		
		if(!is.getLocations().getCenterIsland().equals(e.getBlock().getLocation())) return;
		
		e.getBlock().getDrops().forEach(
				k -> e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().clone().add(0, 1, 0), k));
		
		e.setCancelled(true);
		
		is.getIslandStats().setNbBlock(is.getIslandStats().getNbBlock()+1);
		ToolsIsland.updateIslandInFile(is);
		ToolsIsland.updateIslandVariable(is);
		
		Optional<Phase> currentPhase = ToolsPhase.getPhase(is.getIslandStats().getPhase());
		Optional<Phase> nextPhase = ToolsPhase.getPhase(is.getIslandStats().getPhase()+1);
		if(currentPhase.isEmpty() || nextPhase.isEmpty()) return;
		
		new RandomChoice(e.getBlock().getLocation(), e.getPlayer(), is.getIslandStats().getPhase());
		Optional<Phase> phaseMax = Main.getPhases().stream().max(Comparator.comparingInt(Phase::id));
		if(!phaseMax.isPresent()) return;
		if(is.getIslandStats().getNbBlock() == nextPhase.get().blockToReach()) {
			IslandStats stat = is.getIslandStats();
			stat.setPhase(is.getIslandStats().getPhase()+1);
			is.setIslandStats(stat);
			ToolsIsland.updateIslandInFile(is);
			ToolsIsland.updateIslandVariable(is);
			EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
			map.put(PlaceHolderType.NEXT_PHASE, is.getIslandStats().getPhase()+"");
			MessageUtil.sendMessage(e.getPlayer(), Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_phase_passed"), map);
		}
		
		
		
		
	}

}
