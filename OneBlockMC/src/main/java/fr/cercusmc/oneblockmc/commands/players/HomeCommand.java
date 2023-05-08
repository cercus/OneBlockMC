package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.Position;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class HomeCommand implements SubCommand {

	@Override
	public String getName() {
		return "home";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(Arrays.asList("h"));
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.home.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.home.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		
		Player p = (Player) sender;
		if(OneBlockCommand.messageTooManyArgs(args, p, this)) return;
		
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if(is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
		} else {
			Location home = is.getLocations().getHomeIsland();
			if(home == null) {
				MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_home"));
			} else {
				p.teleport(Position.getCenterOfBlock(home.clone().add(0, 1, 0)));
				MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_teleport_home"));
			}
		}
	}

	@Override
	public String getPermission() {
		return "oneblock.player.home";
	}

}
