package fr.cercusmc.oneblockmc.utils;

import org.bukkit.Location;
import org.bukkit.World;

import fr.cercusmc.oneblockmc.Main;

public record Position(int x, int y, int z) {
	
	/**
     * Permet de trouver la prochaine île disponible selon un algorithme de grille en spirale :
     * 15 16 17 18
     * 4  3  2  11
     * 5  0  1  10
     * 6  7  8  9
     * @param num : Nombre d'île maximum
     * @return La position pour la nouvelle île
     */
	public static Position findNext(int num){
        // Direction du mouvement de la spirale
        int dx = 0;
        int dz = 2 * Main.getInstance().getConfig().getInt("oneblock.space_between_islands");

        // Taille du segment courant
        int segmentLength = 1;

        // Position actuelle
        int x = 0;
        int z = 0;
        // Nombre de segments passés
        int segmentPassed = 0;
        // Si c'est le centre
        if(num == 0) {
            return new Position(x, 73, z);
        }

        for(int n = 0; n < num; ++n){

            // Incrementation de la position actuelle
            x += dx;
            z += dz;
            ++segmentPassed;
            if(segmentLength == segmentPassed) {
                segmentPassed = 0;

                // On change de direction
                int buffer = dz;
                dz = -dx;
                dx = buffer;

                // Augmentation de la taille du segment si necessaire
                if(dx == 0)
                    ++segmentLength;
            }
        }
        return new Position(x, 73, z);

    }
	
	public Location toLocation(World world) {
		return new Location(world, x(), y(), z());
	}

}
