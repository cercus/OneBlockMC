package fr.cercusmc.oneblockmc.menus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.phases.Phase;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.ItemBuilder;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.enums.PlaceHolderType;
import fr.cercusmc.oneblockmc.utils.exceptions.MenuException;
import fr.cercusmc.oneblockmc.utils.menus.PaginatedMenu;
import fr.cercusmc.oneblockmc.utils.menus.PlayerMenuUtility;

public class PhaseMenu extends PaginatedMenu {

	public PhaseMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
	}
	
	@Override
	public List<ItemStack> getData() {
		List<ItemStack> its = new ArrayList<>();
		
		Island is = ToolsIsland.getIslandOfPlayer(playerMenuUtility.getOwner().getUniqueId());
		if(is == null) return new ArrayList<>();
		for(Phase p : Main.getPhases()) {
			
			ItemBuilder it = new ItemBuilder(p.icon(), 1).setName(p.name()).setLores(p.description());
			if(is.getIslandStats().getPhase() == p.id())
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
		return "Liste des phases";
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
