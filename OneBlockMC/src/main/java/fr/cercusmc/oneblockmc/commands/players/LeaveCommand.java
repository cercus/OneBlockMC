package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.List;

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

public class LeaveCommand implements SubCommand {

	@Override
	public String getName() {
		return "leave";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.leave.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.leave.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		
		Player p = (Player) sender;
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if (OneBlockCommand.messageTooManyArgs(args, p, this, 2))
			return;
		if (is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
			return;
		}
		
		if(is.getOwner().equals(p.getUniqueId())) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.cant_leave_island_if_owner"));
			return;
		}
		
		is.getMembers().remove(p.getUniqueId().toString());
		ToolsIsland.updateIslandInFile(is);
		ToolsIsland.updateIslandVariable(is);
		p.teleport(Position.getCenterOfBlock(Main.getIslandConfig().getSpawn()));
		MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_leave_island"));
		
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.leave";
	}

}
