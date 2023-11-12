package fr.cercusmc.oneblockmc.commands.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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

public class InfosCommand implements SubCommand {

	@Override
	public String getName() {
		return "infos";
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>(Arrays.asList("info", "i"));
	}

	@Override
	public String getDescription() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.infos.description");
	}

	@Override
	public String getSyntax() {
		return Main.getFiles().get(Constantes.MESSAGES).getString("commands.infos.syntax");
	}

	@Override
	public void perform(CommandSender sender, String[] args) {

		Player p = (Player) sender;
		if (OneBlockCommand.messageTooManyArgs(args, p, this, 2))
			return;

		Island is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		if (is == null) {
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("island.not_island"));
			return;
		}

		EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
		map.put(PlaceHolderType.ISLAND_NAME, is.getIslandCustomName());
		map.put(PlaceHolderType.ISLAND_OWNER, Bukkit.getPlayer(is.getOwner()).getName());
		map.put(PlaceHolderType.LEVEL, is.getIslandStats().getLevel() + "");
		map.put(PlaceHolderType.CURRENT_PHASE, is.getIslandStats().getPhase() + "");
		map.put(PlaceHolderType.NEXT_PHASE, (is.getIslandStats().getPhase() + 1) + "");
		map.put(PlaceHolderType.NB_BLOCK, is.getIslandStats().getNbBlock() + "");
		map.put(PlaceHolderType.ISLAND_RADIUS, is.getIslandStats().getRadiusIsland() + "");
		map.put(PlaceHolderType.BIOME_NAME, is.getBiome().name());
		map.put(PlaceHolderType.MEMBERS, is.getMembers().stream().map(UUID::fromString).map(Bukkit::getPlayer)
				.map(Player::getName).toList().toString());

		MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getStringList("island.infos"), map);

	}

	@Override
	public String getPermission() {
		return "oneblock.player.info";
	}

}
