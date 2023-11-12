package fr.cercusmc.oneblockmc.utils;

import org.bukkit.entity.Player;

public class ExperienceUtil {
	
	private ExperienceUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

	/**
	 * Get the experience missing to level up
	 * @param level
	 * @return
	 */
    public static int getExpToLevelUp(int level){
    	if(level <= 15) return 2*level+7;
    	return level <= 30 ? 5*level-38 : 9*level-158;
    }

    /**
     * Get the experience at level given
     * @param level
     * @return
     */
    public static int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }

    /**
     * Get the total exp of player
     * @param player
     * @return
     */
    public static int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();

        // Get the amount of XP in past levels
        exp += getExpAtLevel(level);

        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.getExp());

        return exp;
    }

    /**
     * Give some experiences to player
     * @param p
     * @param experience
     */
    public static boolean giveExperience(Player p, int experience) {
        int currentExp = getPlayerExp(p);
        p.setExp(0);
        p.setLevel(0);
        int newExp = currentExp + experience;
        if(newExp < 0)
        	return false;
        p.giveExp(newExp);
        return true;
    }

    public static boolean removeExperience(Player p, int experience){
        int currentExp = getPlayerExp(p);
        if(currentExp < experience) return false;

        p.setExp(0);
        p.setLevel(0);
        int newExp = currentExp-experience;
        if(newExp < 0) return false;
        p.giveExp(newExp);
        return true;

    }
    
    public static boolean giveLevel(Player p, int level) {
    	p.setLevel(p.getLevel()+level);
        return true;
    }

    public static boolean removeLevel(Player p, int level) {
        if(p.getLevel() < Math.abs(level))
            return false;
        p.setLevel(p.getLevel()-Math.abs(level));
        return true;
    }

}
