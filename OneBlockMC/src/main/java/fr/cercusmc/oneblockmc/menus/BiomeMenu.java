package fr.cercusmc.oneblockmc.menus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.FileCustom;
import fr.cercusmc.oneblockmc.utils.ItemBuilder;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.PlaceHolderType;
import fr.cercusmc.oneblockmc.utils.ValidateUtil;
import fr.cercusmc.oneblockmc.utils.exceptions.MenuException;
import fr.cercusmc.oneblockmc.utils.menus.PaginatedMenu;
import fr.cercusmc.oneblockmc.utils.menus.PlayerMenuUtility;

public class BiomeMenu extends PaginatedMenu {

	private static final String BIOMES = "biomes.";

	public BiomeMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);

	}

	@Override
	public List<ItemStack> getData() {
		List<ItemStack> its = new ArrayList<>();
		final FileCustom fileCustom = Main.getFiles().get(Constantes.BIOMES);
		for (String i : fileCustom.getConfigurationSection(Constantes.BIOMES).getKeys(false)) {
			final String pathBiome = Constantes.BIOMES + "." + i;
			String icon = fileCustom.getString(pathBiome + ".icon");
			String name = MessageUtil.format(fileCustom.getString(pathBiome + ".name"));
			List<String> lore = MessageUtil.format(fileCustom.getStringList(pathBiome + ".description"));

			if (!ValidateUtil.checkEnum(Material.class, icon.toUpperCase()))
				continue;
			ItemBuilder it = new ItemBuilder(Material.valueOf(icon));
			it.setName(name).setLores(lore);
			Island is = ToolsIsland.getIslandOfPlayer(playerMenuUtility.getOwner().getUniqueId());
			if (is.getBiome().name().equals(i))
				it.addEnchantment(Enchantment.ARROW_DAMAGE, 1).addFlag(ItemFlag.HIDE_ENCHANTS);
			its.add(it.toItemStack());

		}
		this.data = its;
		return its;
	}

	@Override
	public void loopCode(Object object) {
		inventory.addItem((ItemStack) object);
	}

	@Override
	public Map<Integer, ItemStack> getCustomMenuBorderItems() {
		return new HashMap<>();
	}

	@Override
	public String getMenuName() {
		return "Liste des biomes";
	}

	@Override
	public int getSlots() {
		return 9 * 6;
	}

	@Override
	public boolean cancelAllClicks() {
		return false;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) throws MenuException {
		e.setCancelled(true);
		if (e.getCurrentItem() == null)
			return;

		EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
		map.put(PlaceHolderType.PAGE, page + "");
		map.put(PlaceHolderType.MAX_PAGE, Math.ceil(getData().size() * 1.0 / getMaxItemsPerPage()) + "");

		String name = e.getCurrentItem().getItemMeta().getDisplayName();
		if (name.equals(MessageUtil
				.format(Main.getFiles().get(Constantes.MESSAGES).getString("menus.icon_previous_page.name")))) {

			if (prevPage())
				MessageUtil.sendMessage((Player) e.getWhoClicked(),
						Main.getFiles().get(Constantes.MESSAGES).getString("current_page"), map);
			else
				MessageUtil.sendMessage((Player) e.getWhoClicked(),
						Main.getFiles().get(Constantes.MESSAGES).getString("first_page"));

		} else if (name.equals(
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getString("menus.icon_next_page.name")))) {
			if (nextPage())
				MessageUtil.sendMessage((Player) e.getWhoClicked(),
						Main.getFiles().get(Constantes.MESSAGES).getString("current_page"), map);

			else
				MessageUtil.sendMessage((Player) e.getWhoClicked(),
						Main.getFiles().get(Constantes.MESSAGES).getString("last_page"));

		} else if (name.equals(
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getString("menus.close_menu.name")))) {
			e.getWhoClicked().closeInventory();
		} else {
			String materialName = e.getCurrentItem().getType().name();
			Optional<Biome> biomeChoose = getBiomeByMaterialAndName(materialName, name);
			final boolean changeBiome = biomeChoose.isPresent()
					&& Main.getFiles().get(Constantes.BIOMES).getBoolean(BIOMES + biomeChoose.get().name() + ".permission")
					&& e.getWhoClicked().hasPermission("oneblock.biome." + biomeChoose.get().name().toLowerCase());
			
			if (changeBiome) 
				ToolsIsland.changeBiomeOfIsland(e.getWhoClicked().getUniqueId(), biomeChoose.get());

		}

	}

	private Optional<Biome> getBiomeByMaterialAndName(String material, String name) {
		final FileCustom fileCustom = Main.getFiles().get("biomes");
		for (String i : fileCustom.getConfigurationSection("biomes").getKeys(false)) {

			if (ValidateUtil.checkEnum(Biome.class, i) && fileCustom.getString(BIOMES + i + ".name").equals(name)
					&& fileCustom.getString(BIOMES + i + ".icon").equals(material))
				return Optional.of(Biome.valueOf(i));
		}

		return Optional.empty();

	}

}
