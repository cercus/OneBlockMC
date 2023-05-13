package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.Locations;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class DelHomeCommand implements SubCommand {

	@Override
	public String getName() {
		return "delhome";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(Arrays.asList("dh"));
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.delhome.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.delhome.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if (is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
			return;
		}
		if (!is.getOwner().equals(p.getUniqueId())) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_owner"));
			return;
		}
		
		Locations locs = is.getLocations();
		locs.setHomeIsland(null);
		is.setLocations(locs);
		ToolsIsland.updateIslandInFile(is);
		ToolsIsland.updateIslandVariable(is);
		MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_delete_home"));
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.delhome";
	}

}
