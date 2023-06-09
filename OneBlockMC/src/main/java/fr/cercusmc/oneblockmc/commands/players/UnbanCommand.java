package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.SubCommand;
import fr.cercusmc.oneblockmc.utils.enums.PlaceHolderType;

public class UnbanCommand implements SubCommand {
	
	@Override
	public String getName() {
		return "unban";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.unban.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.unban.syntax");
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
		
		if(!is.playerIsOwner(p.getUniqueId())) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_owner"));
			return;
		}
		
		Optional<OfflinePlayer> offlinePlayer = Arrays.asList(Bukkit.getOfflinePlayers()).stream().filter(k -> k.getName().equals(args[1])).findFirst();
		if(offlinePlayer.isPresent()) {
			if(is.getBans().contains(offlinePlayer.get().getUniqueId().toString())) {
				is.removeBan(offlinePlayer.get().getUniqueId().toString());
				ToolsIsland.updateIslandInFile(is);
				ToolsIsland.updateIslandVariable(is);
				EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
				map.put(PlaceHolderType.PLAYER, offlinePlayer.get().getName());
				MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_unbanned"), map);
				map.replace(PlaceHolderType.PLAYER, p.getName());
				MessageUtil.sendMessage(offlinePlayer.get().getPlayer(), Main.getFiles().get(Constantes.MESSAGES).getString("island.player_unbanned"), map);	
			} else {
				MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.player_already_unbanned"));
			}
		} else {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.player_doesnt_exist"));
		}
		
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.ban";
	}

}
