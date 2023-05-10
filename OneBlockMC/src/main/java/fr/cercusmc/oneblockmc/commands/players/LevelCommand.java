package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class LevelCommand implements SubCommand {
	

	@Override
	public String getName() {
		return "level";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(Arrays.asList("lvl"));
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.level.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.level.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		
		Player p = (Player) sender;
		if(OneBlockCommand.messageTooManyArgs(args, p, this, 2)) return;
		
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if(is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
		} else {			
			ToolsIsland.calcLevel(p.getUniqueId());
		}
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.level";
	}

}
