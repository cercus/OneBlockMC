package fr.cercusmc.oneblockmc.islands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.ValidateUtil;

public class IslandConfig {

	private String overworld;
	private Biome biomeDefault;
	private int positionY;
	private int radiusMin;
	private int radiusMax;
	private int spaceBetweenIsland;
	private Location spawn;
	private Material iconFillerMenu;
	private String nameFillerMenu;
	private List<String> loreFillerMenu;

	private int probabilityGenerateBlock;
	private int probabilityGenerateChest;
	private int probabilityGenerateEntities;
	private int minItemInChest;
	private int maxItemInChest;
	private int minCountOneItemInChest;
	private int maxCountOneItemInChest;
	private int minEnchantmentOneEnchantedBook;
	private int maxEnchantmentOneEnchantedBook;
	private Particle particleGenerateBlock;
	private Particle particleGenerateEntity;
	private Sound soundGenerateEntity;

	public IslandConfig() {
		final FileConfiguration config = Main.getInstance().getConfig();
		this.overworld = config.getString("config.overworld");
		this.biomeDefault = Biome.valueOf(config.getString("config.biome_default"));
		this.positionY = config.getInt("config.positionY");
		this.radiusMin = config.getInt("config.radiusMin");
		this.radiusMax = config.getInt("config.radiusMax");
		this.spaceBetweenIsland = config.getInt("config.space_between_island");
		this.spawn = new Location(Bukkit.getWorld(config.getString("config.spawn.world")),
				config.getInt("config.spawn.x"), config.getInt("config.spawn.y"), config.getInt("config.spawn.z"));
		this.iconFillerMenu = Material.valueOf(config.getString("config.menus.filler.icon"));
		this.nameFillerMenu = config.getString("config.menus.filler.name");
		this.loreFillerMenu = config.getStringList("config.menus.filler.lore");

		this.probabilityGenerateBlock = config.getInt("config.random_block.probability_generate_block");
		this.probabilityGenerateChest = config.getInt("config.random_block.probability_generate_chest");
		this.probabilityGenerateEntities = config.getInt("config.random_block.probability_generate_entities");
		this.minItemInChest = config.getInt("config.random_block.min_item_in_chest");
		this.maxItemInChest = config.getInt("config.random_block.max_item_in_chest");
		this.minCountOneItemInChest = config.getInt("config.random_block.min_count_one_item_in_chest");
		this.maxCountOneItemInChest = config.getInt("config.random_block.max_count_one_item_in_chest");
		this.minEnchantmentOneEnchantedBook = config.getInt("config.random_block.min_enchantment_one_enchanted_book");
		this.maxEnchantmentOneEnchantedBook = config.getInt("config.random_block.max_enchantment_one_enchanted_book");

		this.particleGenerateBlock = ValidateUtil.checkEnum(Particle.class,
				config.getString("config.random_block.particle_generate_block"))
						? Particle.valueOf(config.getString("config.random_block.particle_generate_block"))
						: null;

		this.particleGenerateEntity = ValidateUtil.checkEnum(Particle.class,
				config.getString("config.random_block.particle_generate_entity"))
						? Particle.valueOf(config.getString("config.random_block.particle_generate_entity"))
						: null;

		this.soundGenerateEntity = ValidateUtil.checkEnum(Sound.class,
				config.getString("config.random_block.sound_generate_entity"))
						? Sound.valueOf(config.getString("config.random_block.sound_generate_entity"))
						: null;
	}

	public Biome getBiomeDefault() {
		return biomeDefault;
	}

	public int getPositionY() {
		return positionY;
	}

	public String getOverworld() {
		return overworld;
	}

	public int getRadiusMax() {
		return radiusMax;
	}

	public int getRadiusMin() {
		return radiusMin;
	}

	public int getSpaceBetweenIsland() {
		return spaceBetweenIsland;
	}

	public Location getSpawn() {
		return spawn;
	}

	public Material getIconFillerMenu() {
		return iconFillerMenu;
	}

	public List<String> getLoreFillerMenu() {
		return loreFillerMenu;
	}

	public String getNameFillerMenu() {
		return nameFillerMenu;
	}

	public int getProbabilityGenerateBlock() {
		return probabilityGenerateBlock;
	}

	public int getProbabilityGenerateChest() {
		return probabilityGenerateChest;
	}

	public int getProbabilityGenerateEntities() {
		return probabilityGenerateEntities;
	}

	public int getMinItemInChest() {
		return minItemInChest;
	}

	public int getMaxItemInChest() {
		return maxItemInChest;
	}

	public int getMinCountOneItemInChest() {
		return minCountOneItemInChest;
	}

	public int getMaxCountOneItemInChest() {
		return maxCountOneItemInChest;
	}

	public int getMinEnchantmentOneEnchantedBook() {
		return minEnchantmentOneEnchantedBook;
	}

	public int getMaxEnchantmentOneEnchantedBook() {
		return maxEnchantmentOneEnchantedBook;
	}

	public Particle getParticleGenerateBlock() {
		return particleGenerateBlock;
	}

	public Particle getParticleGenerateEntity() {
		return particleGenerateEntity;
	}
	
	public Sound getSoundGenerateEntity() {
		return soundGenerateEntity;
	}

}
