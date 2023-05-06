package fr.cercusmc.oneblockmc.utils;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface SubCommand {
	
	/**
     * @return The name of the subcommand
     */
    public abstract String getName();

    /**
     * @return The aliases that can be used for this command. Can be null
     */
    public abstract List<String> getAliases();

    /**
     * @return A description of what the subcommand does to be displayed
     */
    public abstract String getDescription();

    /**
     * @return An example of how to use the subcommand
     */
    public abstract String getSyntax();

    /**
     * @param sender The thing that ran the command
     * @param args   The args passed into the command when run
     */
    public abstract void perform(CommandSender sender, String[] args);
    
    public default boolean playerHasPermission(CommandSender sender, String permission) {
    	return sender.hasPermission(permission); 
    			
    }
    
    public abstract String getPermission();


}
