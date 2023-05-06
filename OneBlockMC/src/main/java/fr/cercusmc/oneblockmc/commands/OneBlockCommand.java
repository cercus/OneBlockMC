package fr.cercusmc.oneblockmc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Position;
import fr.cercusmc.oneblockmc.utils.SubCommand;

public class OneBlockCommand implements CommandExecutor, TabCompleter {
	
	private ArrayList<SubCommand> subCommands;
	private ArrayList<SubCommand> subCommandsAdmin;

	public OneBlockCommand() {
		this.subCommands = new ArrayList<>();
		this.subCommandsAdmin = new ArrayList<>();
		
		

		
	}

	@SuppressWarnings("unused")
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
				Optional<SubCommand> sub = subCommandsAdmin.stream().filter(k-> k.getName().equals(args[0])).findFirst();
				
				if(sub.isPresent() && sub.get().playerHasPermission(p, sub.get().getPermission()) || p.isOp()) 
					sub.ifPresent(k -> k.perform(p, args));
					
			} else if(args.length > 0) {
				Optional<SubCommand> sub = subCommands.stream().filter(k-> k.getName().equals(args[0])).findFirst();
				if(sub.isPresent() && sub.get().playerHasPermission(p, sub.get().getPermission())) 
					sub.ifPresent(k -> k.perform(p, args));

			} else {
				teleportPlayerIsland(p);

			}
		}

		return true;
	}

	private void teleportPlayerIsland(Player p) {
		Island is = null;
		if(ToolsIsland.getIslandOfPlayer(p.getUniqueId()) == null) {
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
        for(SubCommand s : subCommands)
            subCmds.add(s.getName());
        
        if(args.length == 1) {
            list = StringUtil.copyPartialMatches(args[0], subCmds, list);
        } else if(args[0].equalsIgnoreCase("admin") && sender.isOp())
            list = StringUtil.copyPartialMatches(args[1], subCmdsAdmin, list);
        return list;
	}

}