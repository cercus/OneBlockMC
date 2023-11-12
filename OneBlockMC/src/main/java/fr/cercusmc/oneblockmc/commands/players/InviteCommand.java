package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cercusmc.oneblockmc.IslandInvite;
import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.commands.OneBlockCommand;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class InviteCommand implements SubCommand {

	@Override
	public String getName() {
		return "invite";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.invite.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.invite.syntax");
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
		
		Optional<Player> targetOptional = Optional.ofNullable(Bukkit.getPlayerExact(args[1]));
		if(targetOptional.isEmpty()) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.player_doesnt_exist"));
			return;
		}
		Player target = targetOptional.get();
		if(target.equals(p)) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.invite_auto_deny"));
			return;
		}
		Optional<IslandInvite> inviteOwner = Main.getInvites().stream().filter(k -> k.getOwner().equals(p.getUniqueId())).findFirst();
		if(inviteOwner.isPresent()) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.player_already_launch_invitation"));
		} else {
			Optional<IslandInvite> inviteMember = Main.getInvites().stream().filter(k -> k.getMemberWhoWantJoin().equals(target.getUniqueId())).findFirst();
			if(inviteMember.isPresent()) {
				MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.player_already_invitation"));
			} else {
				Main.getInvites().add(new IslandInvite(p.getUniqueId(), target.getUniqueId()));
				MessageUtil.sendMessage(target, Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_request_invitation_to_target"), Arrays.asList("%player%"), Arrays.asList(p.getName()));
				MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_request_invitation"));
			}
			
		}
	}

	@Override
	public String getPermission() {
		return "oneblock.player.invite";
	}

}
