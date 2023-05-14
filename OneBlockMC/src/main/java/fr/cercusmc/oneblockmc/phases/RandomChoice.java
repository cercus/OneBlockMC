package fr.cercusmc.oneblockmc.phases;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.ItemBuilder;
import fr.cercusmc.oneblockmc.utils.MathUtil;
import fr.cercusmc.oneblockmc.utils.Position;

public class RandomChoice {

	private GenerateType type;
	private Location loc;
	private Player player;
	private int phase;
	private Random random;

	public RandomChoice(Location loc, Player player, int phase) {
		this.loc = loc;
		this.player = player;
		this.phase = phase;

		try {
			this.random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			this.random = new Random();
		}

		Map<String, Integer> probas = new HashMap<>();
		final int probabilityGenerateBlock = Main.getIslandConfig().getProbabilityGenerateBlock();
		probas.put("block", probabilityGenerateBlock);
		final int probabilityGenerateEntities = Main.getIslandConfig().getProbabilityGenerateEntities();
		probas.put("entity", probabilityGenerateEntities);
		final int probabilityGenerateChest = Main.getIslandConfig().getProbabilityGenerateChest();
		probas.put("chest", probabilityGenerateChest);

		int r = this.random.nextInt(probas.values().stream().mapToInt(Integer::intValue).sum());

		Map<String, Integer> sorted = MathUtil.sortByValue(probas);

		LinkedList<String> keys = new LinkedList<>(sorted.keySet());
		if (r < sorted.get(keys.get(0))) {
			this.type = GenerateType.valueOf(keys.get(0).toUpperCase());
			randomBlock();
		} else if (r < sorted.get(keys.get(0)) + sorted.get(keys.get(1))) {
			this.type = GenerateType.valueOf(keys.get(1).toUpperCase());
			randomEntity();
		} else {
			this.type = GenerateType.valueOf(keys.get(2).toUpperCase());
			randomChest();
		}

	}

	private void randomChest() {
		this.loc.getBlock().breakNaturally();
		this.loc.getBlock().setType(Material.CHEST);
		Chest chest = (Chest) this.loc.getBlock().getState();
		Inventory inv = chest.getInventory();
		Optional<Phase> p = ToolsPhase.getPhase(this.phase);
		if (p.isEmpty())
			return;
		// Generation d'une liste de 0 a 27 inclus
		ArrayList<Integer> slotsChest = new ArrayList<>(
				IntStream.iterate(0, n -> n + 1).limit(inv.getSize()).boxed().toList());
		ArrayList<String> slotsChestString = new ArrayList<>();
		slotsChest.forEach(k -> slotsChestString.add(k + ""));

		for (int i = 0; i < random
				.nextInt(Main.getIslandConfig().getMaxItemInChest() - Main.getIslandConfig().getMinItemInChest())
				+ Main.getIslandConfig().getMinItemInChest(); i++) {

			int slotChosen = random.nextInt(slotsChestString.size());
			ItemStack itemChosen = p.get().items().get(random.nextInt(p.get().items().size()));
			ItemBuilder itBuilder = ItemBuilder.toItemBuilder(itemChosen);
			if(itemChosen.getType().equals(Material.ENCHANTED_BOOK) && itemChosen.getEnchantments().isEmpty()) {
					List<Enchantment> enchsAvailable = Arrays.asList(Enchantment.values());
					itBuilder.addEnchantment(enchsAvailable.get(random.nextInt(enchsAvailable.size())), random.nextInt(5)+1);
					itemChosen = itBuilder.toItemStack();
				
			}
			
			itemChosen.setAmount(random
					.nextInt(Main.getIslandConfig().getMaxCountOneItemInChest()
							- Main.getIslandConfig().getMinCountOneItemInChest())
					+ Main.getIslandConfig().getMinCountOneItemInChest());
			inv.setItem(slotChosen, itemChosen);
			slotsChestString.remove(slotChosen+"");
		}
	}

	private Entity randomEntity() {

		Optional<Phase> p = ToolsPhase.getPhase(this.phase);

		if (p.isEmpty())
			return null;

		EntityType choice = ToolsPhase.selectionAleatoirePondere(p.get().mobs());
		Entity entity = this.loc.getWorld().spawnEntity(Position.getCenterOfBlock(this.loc.clone().add(0, 2, 0)), choice);
		this.loc.getWorld().spawnParticle(Main.getIslandConfig().getParticleGenerateEntity(),
				this.loc.clone().add(0, 1, 0), 50);
		this.loc.getWorld().playSound(player.getLocation(), Main.getIslandConfig().getSoundGenerateEntity(), 1.0f,
				1.0f);
		this.loc.getBlock().setType(randomBlock().getType());

		return entity;
	}

	private Block randomBlock() {

		Optional<Phase> p = ToolsPhase.getPhase(this.phase);
		
		if (p.isEmpty()) {
			this.loc.getBlock().setType(Material.GRASS_BLOCK);
			return this.loc.getBlock();
		}

		Material choice = ToolsPhase.selectionAleatoirePondere(p.get().blocs());
		this.loc.getBlock().setType(choice);
		this.loc.getWorld().spawnParticle(Main.getIslandConfig().getParticleGenerateBlock(),
				this.loc.clone().add(0, 1, 0), 50);
		return this.loc.getBlock();
	}

	public GenerateType getType() {
		return type;
	}

	public void setType(GenerateType type) {
		this.type = type;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

}
