package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class DeleteCommand implements SubCommand {

	private static final String MESSAGES = "messages";

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
		return Main.getFiles().get(MESSAGES).getString("commands.delete.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(MESSAGES).getString("commands.delete.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if(is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(MESSAGES).getString("island.not_island"));
			
		} else {
			if(is.playerIsOwner(p.getUniqueId())) {
				ToolsIsland.deleteIsland(p.getUniqueId());
			} else {
				MessageUtil.sendMessage(p, Main.getFiles().get(MESSAGES).getString("island.not_owner"));
			}
		}
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.delete";
	}

}
