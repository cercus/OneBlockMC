package fr.cercusmc.oneblockmc.phases;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public record Phase(int id, Material icon, String name, int blockToReach, List<String> description,
		Map<Material, Integer> blocs, Map<EntityType, Integer> mobs, List<ItemStack> items) {
}
