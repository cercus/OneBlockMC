package fr.cercusmc.oneblockmc.menus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.settings.Setting;
import fr.cercusmc.oneblockmc.settings.ToolSettings;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.files.FileCustom;
import fr.cercusmc.oneblockmc.utils.items.ItemBuilder;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.enums.PlaceHolderType;
import fr.cercusmc.oneblockmc.utils.exceptions.MenuException;
import fr.cercusmc.oneblockmc.utils.menus.PaginatedMenu;
import fr.cercusmc.oneblockmc.utils.menus.PlayerMenuUtility;

public class SettingsMenu extends PaginatedMenu {

	public SettingsMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
	}

	@Override
	public List<ItemStack> getData() {
		
		List<ItemStack> its = new ArrayList<>();
		FileCustom fileCustom = Main.getFiles().get("settings");
		List<Setting> settings = ToolSettings.getAllSettings(fileCustom);
		for(Setting s : settings) {
			ItemBuilder it = new ItemBuilder(s.getIcon(), 1, s.getDescription());
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
		return "Configuration";
	}

	@Override
	public int getSlots() {
		return 9*6;
	}

	@Override
	public boolean cancelAllClicks() {
		return true;
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
						Main.getFiles().get(Constantes.MESSAGES).getString("menus.current_page"), map);
			else
				MessageUtil.sendMessage((Player) e.getWhoClicked(),
						Main.getFiles().get(Constantes.MESSAGES).getString("menus.first_page"));

		} else if (name.equals(
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getString("menus.icon_next_page.name")))) {
			if (nextPage())
				MessageUtil.sendMessage((Player) e.getWhoClicked(),
						Main.getFiles().get(Constantes.MESSAGES).getString("menus.current_page"), map);

			else
				MessageUtil.sendMessage((Player) e.getWhoClicked(),
						Main.getFiles().get(Constantes.MESSAGES).getString("menus.last_page"));

		} else if (name.equals(
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getString("menus.close_menu.name")))) {
			e.getWhoClicked().closeInventory();
		}
	}

}
