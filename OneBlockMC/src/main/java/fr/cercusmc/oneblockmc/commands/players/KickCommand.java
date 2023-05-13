package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.PlaceHolderType;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class KickCommand implements SubCommand {

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.kick.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.kick.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		if(OneBlockCommand.messageTooManyArgs(args, p, this, 3) || args.length == 1) return;
		
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if(is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
			return;
		}
		
		Optional<Player> target = Optional.ofNullable(Bukkit.getPlayerExact(args[1]));
		if(target.isPresent()) {
			target.get().teleport(Main.getIslandConfig().getSpawn());
			EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
			map.put(PlaceHolderType.PLAYER, target.get().getName());
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_kicked"), map);
			
			EnumMap<PlaceHolderType, String> map2 = new EnumMap<>(PlaceHolderType.class);
			map.put(PlaceHolderType.PLAYER, p.getName());
			MessageUtil.sendMessage(target.get(), Main.getFiles().get(Constantes.MESSAGES).getString("island.player_kicked"), map2);
		} else {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.player_doesnt_exist"));
		}
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.kick";
	}

}
