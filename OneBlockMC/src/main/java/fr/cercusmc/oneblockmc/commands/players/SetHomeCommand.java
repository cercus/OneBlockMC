package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.Locations;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.PlaceHolderType;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class SetHomeCommand implements SubCommand {

	@Override
	public String getName() {
		return "sethome";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(Arrays.asList("sh"));
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.sethome.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.sethome.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {

		Player p = (Player) sender;
		if (OneBlockCommand.messageTooManyArgs(args, p, this))
			return;

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
		locs.setHomeIsland(p.getLocation());
		is.setLocations(locs);
		ToolsIsland.updateIslandInFile(is);
		ToolsIsland.updateIslandVariable(is);
		EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
		map.put(PlaceHolderType.LOC_X, p.getLocation().getX() + "");
		map.put(PlaceHolderType.LOC_Y, p.getLocation().getY() + "");
		map.put(PlaceHolderType.LOC_Z, p.getLocation().getZ() + "");
		MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_change_home"),
				map);

	}

	@Override
	public String getPermission() {
		return "oneblock.player.sethome";
	}

}
