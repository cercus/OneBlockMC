package fr.cercusmc.oneblockmc.utils.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	private String name;
	private List<String> lores;
	private Material material;
	private ItemStack itemStack;
	private int count;
	private Map<Enchantment, Integer> enchantments;
	private ItemMeta itemMeta;
	private List<ItemFlag> flags;

	public ItemBuilder(Material material, int count) {
		this.material = material;
		this.count = count;
		this.itemStack = new ItemStack(this.material, this.count);
		this.itemMeta = this.itemStack.getItemMeta();
		this.enchantments = new HashMap<>();
		this.flags = new ArrayList<>();
	}

	public ItemBuilder(Material material) {
		this(material, 1);
	}

	public ItemBuilder(Material material, int count, String name) {
		this(material, count);
		this.name = name;
	}

	public ItemBuilder(Material material, int count, String name, List<String> lores) {
		this(material, count, name);
		this.lores = lores;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int power) {
		this.enchantments.put(enchantment, power);
		return this;
	}


	public ItemBuilder addFlag(ItemFlag flag) {
		this.flags.add(flag);
		return this;
	}

	public ItemStack toItemStack() {
		this.itemMeta.setDisplayName(this.name);
		this.itemMeta.setLore(this.lores);
		this.enchantments.forEach((k, v) -> this.itemMeta.addEnchant(k, v, true));
		this.flags.forEach(k -> this.itemMeta.addItemFlags(k));
		this.itemStack.setItemMeta(this.itemMeta);
		return this.itemStack;

	}
	
	public ItemStack toEnchantedBook() {
		if(this.material.equals(Material.ENCHANTED_BOOK)) {
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.itemMeta;
			this.enchantments.forEach((k, v) -> meta.addEnchant(k, v, true));
			this.itemStack.setItemMeta(meta);
		}
		
		return this.itemStack;
	}

	public static ItemBuilder toItemBuilder(ItemStack itemStack) {
		ItemBuilder itemBuilder = new ItemBuilder(itemStack.getType(), itemStack.getAmount(),
				itemStack.getItemMeta().getDisplayName(), itemStack.getItemMeta().getLore());

		itemBuilder.setEnchantments(new HashMap<>(itemStack.getEnchantments()));
		itemBuilder.setItemMeta(itemStack.getItemMeta());

		return itemBuilder;
	}

	public ItemBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ItemBuilder setCount(int count) {
		this.count = count;
		return this;
	}

	public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
		return this;
	}

	public ItemBuilder setItemMeta(ItemMeta itemMeta) {
		this.itemMeta = itemMeta;
		return this;
	}

	public ItemBuilder setLores(List<String> lores) {
		this.lores = new ArrayList<>();
		this.lores = lores;
		return this;
	}

	public ItemBuilder setMaterial(Material material) {
		this.material = material;
		return this;
	}

	public String getName() {
		return name;
	}

	public Material getMaterial() {
		return material;
	}

	public int getCount() {
		return count;
	}

	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

	public ItemMeta getItemMeta() {
		return itemMeta;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public List<String> getLores() {
		return lores;
	}

}
