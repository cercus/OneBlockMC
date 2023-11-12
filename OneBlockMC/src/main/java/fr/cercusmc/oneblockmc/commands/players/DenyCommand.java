package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

public class DenyCommand implements SubCommand {

	@Override
	public String getName() {
		return "deny";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.deny.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.deny.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		
		Player p = (Player) sender;
		if (OneBlockCommand.messageTooManyArgs(args, p, this, 2))
			return;
		
		Optional<IslandInvite> invite = Main.getInvites().stream()
				.filter(k -> k.getMemberWhoWantJoin().equals(p.getUniqueId())).findFirst();
		
		if(!invite.isPresent()) {
			MessageUtil.sendMessage(p,
					Main.getFiles().get(Constantes.MESSAGES).getString("island.no_request_invitation"));
			return;
		}
		
		Main.getInvites().remove(invite.get());
		MessageUtil.sendMessage(p,
				Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_deny_invitation_player"));
		MessageUtil.sendMessage(invite.get().getOwner(),
				Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_deny_invitation_requester"));
		
	}

	@Override
	public String getPermission() {
		return "oneblock.player.deny";
	}

}
