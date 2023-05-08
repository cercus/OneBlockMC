package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class DeleteCommand implements SubCommand {


	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(Arrays.asList("del"));
		
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.delete.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.delete.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		
		Player p = (Player) sender;
		if(OneBlockCommand.messageTooManyArgs(args, p, this)) return;
		
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if(is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
			
		} else {
			if(is.playerIsOwner(p.getUniqueId())) {
				ToolsIsland.deleteIsland(is, p.getUniqueId());
				for(String member : is.getMembers()) {
					if(Bukkit.getPlayer(UUID.fromString(member)).isOnline() && !p.getUniqueId().toString().equals(member)) {
						Bukkit.getPlayer(UUID.fromString(member)).teleport(Main.getIslandConfig().getSpawn());
						MessageUtil.sendMessage(Bukkit.getPlayer(UUID.fromString(member)), Main.getFiles().get(Constantes.MESSAGES).getString("island.message_member_deleted_island"));
					} else {
						Set<String> players = Main.getFiles().get("deletePlayerWaiting").getStringList("players").stream().collect(Collectors.toSet());
						players.add(member);
						Main.getFiles().get("deletePlayerWaiting").set("players", players);
					}
				}
			} else {
				MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_owner"));
			}
		}
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.delete";
	}

}
