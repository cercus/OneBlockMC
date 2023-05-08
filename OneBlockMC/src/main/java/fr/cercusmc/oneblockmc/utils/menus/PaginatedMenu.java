package fr.cercusmc.oneblockmc.utils.menus;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.utils.Constantes;
import fr.cercusmc.oneblockmc.utils.Logger;
import fr.cercusmc.oneblockmc.utils.MessageUtil;

public abstract class PaginatedMenu extends Menu {

	protected Logger logger = new Logger();

	// The items being paginated
	protected List<ItemStack> data;

	// Keep track of what page the menu is on
	protected int page = 0;
	// 28 is max items because with the border set below,
	// 28 empty slots are remaining.
	protected int maxItemsPerPage = 28;
	// the index represents the index of the slot
	// that the loop is on
	protected int index = 0;

	protected PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
	}

	/**
	 * @return A list of the data being paginated. usually this is a list of items
	 *         but it can be anything
	 */
	public abstract List<ItemStack> getData();

	/**
	 * @param object A single element of the data list that you do something with.
	 *               It is recommended that you turn this into an item if it is not
	 *               already and then put it into the inventory as you would with a
	 *               normal Menu. You can execute any other logic in here as well.
	 */
	public abstract void loopCode(Object object);

	/**
	 * @return A hashmap of items you want to be placed in the paginated menu
	 *         border. This will override any items already placed by default. Key =
	 *         slot, Value = Item
	 */
	public abstract Map<Integer, ItemStack> getCustomMenuBorderItems();

	/**
	 * Set the border and menu buttons for the menu. Override this method to provide
	 * a custom menu border or specify custom items in customMenuBorderItems()
	 */
	protected void addMenuBorder() {

		inventory.setItem(48, makeItem(
				Material.valueOf(Main.getFiles().get(Constantes.MESSAGES).getString("menus.icon_previous_page.icon")),
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getString("menus.icon_previous_page.name")),
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getStringList("menus.icon_previous_page.lore"))));
		inventory.setItem(49, makeItem(
				Material.valueOf(Main.getFiles().get(Constantes.MESSAGES).getString("menus.close_menu.icon")),
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getString("menus.close_menu.name")),
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getStringList("menus.close_menu.lore"))));
		inventory.setItem(50, makeItem(
				Material.valueOf(Main.getFiles().get(Constantes.MESSAGES).getString("menus.icon_next_page.icon")),
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getString("menus.icon_next_page.name")),
				MessageUtil.format(Main.getFiles().get(Constantes.MESSAGES).getStringList("menus.icon_next_page.lore"))));

		for (int i = 0; i < 10; i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, super.fillerGlass);
			}
		}

		inventory.setItem(17, super.fillerGlass);
		inventory.setItem(18, super.fillerGlass);
		inventory.setItem(26, super.fillerGlass);
		inventory.setItem(27, super.fillerGlass);
		inventory.setItem(35, super.fillerGlass);
		inventory.setItem(36, super.fillerGlass);

		for (int i = 44; i < 54; i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, super.fillerGlass);
			}
		}

		// place the custom items if they exist
		if (getCustomMenuBorderItems() != null || getCustomMenuBorderItems().isEmpty()) {
			getCustomMenuBorderItems().forEach((integer, itemStack) -> inventory.setItem(integer, itemStack));
		}

	}

	/**
	 * Place each item in the paginated menu, automatically coded by default but
	 * override if you want custom functionality. Calls the loopCode() method you
	 * define for each item returned in the getData() method
	 */
	@Override
	public void setMenuItems() {

		addMenuBorder();
		if (getData() != null && !getData().isEmpty()) {
			for (int i = 0; i < getMaxItemsPerPage(); i++) {
				index = getMaxItemsPerPage() * page + i;
				logger.info(index + "");
				if (index >= getData().size())
					break;
				if (getData().get(index) != null) {
					loopCode(getData().get(index)); // run the code defined by the user
				}
			}
		}

	}

	/**
	 * @return true if successful, false if already on the first page
	 */
	public boolean prevPage() {
		if (page == 0) {
			return false;
		} else {
			page = page - 1;
			reloadItems();
			return true;
		}
	}

	/**
	 * @return true if successful, false if already on the last page
	 */
	public boolean nextPage() {

		if ((index + 1) < getData().size()) {
			page = page + 1;
			reloadItems();
			return true;
		} else {
			return false;
		}
	}

	public int getMaxItemsPerPage() {
		return maxItemsPerPage;
	}

}
