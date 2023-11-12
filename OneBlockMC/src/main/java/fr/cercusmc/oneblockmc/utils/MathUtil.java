package fr.cercusmc.oneblockmc.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Particle;

public class MathUtil {

	private MathUtil() {
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet().stream()
	        .sorted(Map.Entry.comparingByValue())
	        .collect(Collectors.toMap(
	            Map.Entry::getKey,
	            Map.Entry::getValue,
	            (oldValue, newValue) -> oldValue,
	            LinkedHashMap::new
	        ));
	}
	
	public static List<Double> linspace(double min, double max, int nbPoints) {
		List<Double> res = new ArrayList<>();
		double delta = (max-min)/nbPoints;
		for(double i = min; i <= max; i+=delta) {
			res.add(i);
		}
		
		return res;
		
	}
	
	public static void drawSphere(int radius, Location location, Particle particle) {
		Location loc = location.clone().add(0.5,3,0.5);
		for(double phi=0; phi<=2*Math.PI; phi+=Math.PI/60) {
            for(double theta=0; theta<=Math.PI; theta+=Math.PI/30) {
                Coordinate coords = MathUtil.spheriqueToCartesienCoords(radius, theta, phi);
                loc.add(coords.x(), coords.y(), coords.z());
                loc.getWorld().spawnParticle(particle, loc, 0);
                loc.subtract(coords.x(), coords.y(), coords.z());
                
            }
		}
		
	}
	
	public static Coordinate spheriqueToCartesienCoords(double r, double theta, double phi) {
		double x = r*Math.cos(phi)*Math.sin(theta);
        double y = r*Math.cos(theta) + 1;
        double z = r*Math.sin(theta)*Math.sin(phi);
        return new Coordinate(x, y, z);
	}
	
	
	public static void drawCube(Location location, int r, Particle particle) {
		Location loc = location.clone().add(0.5, 3, 0.5);
		Location corner1 = new Location(loc.getWorld(), loc.getX()-r/2.0, loc.getY()+r/2.0, loc.getZ()+r/2.0);
		Location corner2 = new Location(loc.getWorld(), loc.getX()+r/2.0, loc.getY()-r/2.0, loc.getZ()-r/2.0);
		
		int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
		int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
		int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
		
		int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
		int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
		int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
		
		// Faces bas/haut
		for(double x = minX; x <= maxX; x+=0.2D) {
			for(double z = minZ; z <= maxZ; z+=0.2D) {
				
				drawParticle(loc, x, minY, z, particle);
				drawParticle(loc, x, maxY, z, particle);
			}
		}
		
		// Face avant et arrière
		for(double x = minX; x <= maxX; x+=0.2D) {
			for(double y = minY; y <= maxY; y+=0.2D) {
				drawParticle(loc, x, y, minZ, particle);
				drawParticle(loc, x, y, maxZ, particle);
			}
		}
		
		//faces droite/gauche
		for(double z = minZ; z <= maxZ; z+=0.2D) {
			for(double y = minY; y <= maxY; y+=0.2D) {
				drawParticle(loc, minX, y, z, particle);
				drawParticle(loc, maxX, y, z, particle);
			}
		}
			
	}
	
	private static void drawParticle(Location loc, double x, double y, double z, Particle particle) {
		loc.add(x, y, z);
		loc.getWorld().spawnParticle(particle, loc, 1);
		loc.subtract(x, y, z);
	}

	

}
