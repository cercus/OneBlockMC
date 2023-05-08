package fr.cercusmc.oneblockmc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.commands.players.DelHomeCommand;
import fr.cercusmc.oneblockmc.commands.players.DeleteCommand;
import fr.cercusmc.oneblockmc.commands.players.HomeCommand;
import fr.cercusmc.oneblockmc.commands.players.LevelCommand;
import fr.cercusmc.oneblockmc.commands.players.SetHomeCommand;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.PlaceHolderType;
import fr.cercusmc.oneblockmc.utils.Position;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class OneBlockCommand implements CommandExecutor, TabCompleter {

	private ArrayList<SubCommand> subCommands;
	private ArrayList<SubCommand> subCommandsAdmin;

	public OneBlockCommand() {
		this.subCommands = new ArrayList<>();
		this.subCommandsAdmin = new ArrayList<>();

		addAll(new DeleteCommand(), new LevelCommand(), new HomeCommand(), new SetHomeCommand(), new DelHomeCommand());

	}

	private void addAll(SubCommand... subCommands) {
		this.subCommands.addAll(Arrays.asList(subCommands));
	}

	@SuppressWarnings("unused")
	private void addAllAdmin(SubCommand... subCommands) {
		this.subCommandsAdmin.addAll(Arrays.asList(subCommands));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player p) {

			if (args.length > 0 && args[0].equalsIgnoreCase("admin")) {
				Optional<SubCommand> sub = subCommandsAdmin.stream().filter(k -> checkArg(args[1], k)).findFirst();

				if (sub.isPresent() && sub.get().playerHasPermission(p, sub.get().getPermission()) || p.isOp())
					sub.ifPresent(k -> k.perform(p, args));

			} else if (args.length > 0) {
				Optional<SubCommand> sub = subCommands.stream().filter(k -> checkArg(args[0], k)).findFirst();
				if (sub.isPresent() && sub.get().playerHasPermission(p, sub.get().getPermission()))
					sub.ifPresent(k -> k.perform(p, args));

			} else {
				teleportPlayerIsland(p);

			}
		}

		return true;
	}

	private boolean checkArg(String arg, SubCommand k) {
		return k.getName().equals(arg) || k.getAliases().contains(arg);
	}

	private void teleportPlayerIsland(Player p) {
		Island is = null;
		if (ToolsIsland.getIslandOfPlayer(p.getUniqueId()) == null) {
			is = ToolsIsland.createIsland(p.getUniqueId());
		} else {
			is = ToolsIsland.getIslandOfPlayer(p.getUniqueId());
		}

		p.teleport(Position.getCenterOfBlock(is.getLocations().getCenterIsland()));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> subCmds = new ArrayList<>();
		List<String> subCmdsAdmin = new ArrayList<>();
		ArrayList<String> list = new ArrayList<>();
		for (SubCommand s : subCommands)
			subCmds.add(s.getName());

		if (args.length == 1) {
			list = StringUtil.copyPartialMatches(args[0], subCmds, list);
		} else if (args[0].equalsIgnoreCase("admin") && sender.isOp())
			list = StringUtil.copyPartialMatches(args[1], subCmdsAdmin, list);
		return list;
	}

	public static boolean messageTooManyArgs(String[] args, Player p, SubCommand command) {
		if (args.length > 1) {
			EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
			map.put(PlaceHolderType.SYNTAX, command.getSyntax());
			MessageUtil.sendMessage(p, Main.getFiles().get(Constantes.MESSAGES).getString("commands.too_many_args"),
					map);
			return true;
		}
		return false;
	}

}
