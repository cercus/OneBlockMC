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
import fr.cercusmc.oneblockmc.utils.Position;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class AcceptCommand implements SubCommand {

	@Override
	public String getName() {
		return "accept";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.accept.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.accept.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {

		Player p = (Player) sender;
		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if (OneBlockCommand.messageTooManyArgs(args, p, this, 2))
			return;
		if (is != null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.quit_island_invite"));
			return;
		}

		Optional<IslandInvite> invite = Main.getInvites().stream()
				.filter(k -> k.getMemberWhoWantJoin().equals(p.getUniqueId())).findFirst();
		Optional<IslandInvite> acceptHimself = Main.getInvites().stream()
				.filter(k -> k.getOwner().equals(p.getUniqueId())).findFirst();

		if (acceptHimself.isPresent()) {

		}
		if (!invite.isPresent()) {
			MessageUtil.sendMessage(p,
					Main.getFiles().get(Constantes.MESSAGES).getString("island.no_request_invitation"));
		} else {

			Island isOwner = ToolsIsland.getIslandOfPlayer(invite.get().getOwner());
			isOwner.getMembers().add(invite.get().getMemberWhoWantJoin().toString());

			ToolsIsland.updateIslandInFile(isOwner);
			ToolsIsland.updateIslandVariable(isOwner);

			Bukkit.getPlayer(invite.get().getMemberWhoWantJoin())
					.teleport(Position.getCenterOfBlock(isOwner.getLocations().getCenterIsland().clone().add(0, 2, 0)));
			MessageUtil.sendMessage(p,
					Main.getFiles().get(Constantes.MESSAGES).getString("island.successfull_join_island"),
					Arrays.asList("%player%"), Arrays.asList(Bukkit.getPlayer(isOwner.getOwner()).getName()));

			Main.getInvites().remove(invite.get());

		}
	}

	@Override
	public String getPermission() {
		return "oneblock.player.accept";
	}

}
