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
import fr.cercusmc.oneblockmc.menus.BiomeMenu;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.SubCommand;
import fr.cercusmc.oneblockmc.utils.exceptions.MenuException;
import fr.cercusmc.oneblockmc.utils.menus.MenuManager;

public class BiomeCommand implements SubCommand {

	@Override
	public String getName() {
		return "biomes";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(Arrays.asList("biome"));
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.biomes.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.biomes.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if(OneBlockCommand.messageTooManyArgs(args, p, this, 2)) return;
		if(is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
			return;
		}
		if(!is.playerIsOwner(p.getUniqueId())) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_owner"));
			return;
		}
		try {
			MenuManager.openMenu(BiomeMenu.class, p);
		} catch (MenuException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.biome";
	}

}
