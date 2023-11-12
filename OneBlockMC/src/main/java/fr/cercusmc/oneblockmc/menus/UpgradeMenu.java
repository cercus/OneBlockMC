package fr.cercusmc.oneblockmc.menus;

import java.util.*;

import fr.cercusmc.oneblockmc.utils.ExperienceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.cercusmc.oneblockmc.Main;
import fr.cercusmc.oneblockmc.islands.Island;
import fr.cercusmc.oneblockmc.islands.ToolsIsland;
import fr.cercusmc.oneblockmc.utils.items.ItemBuilder;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.enums.PlaceHolderType;
import fr.cercusmc.oneblockmc.utils.exceptions.MenuException;
import fr.cercusmc.oneblockmc.utils.menus.Menu;
import fr.cercusmc.oneblockmc.utils.menus.PlayerMenuUtility;

public class UpgradeMenu extends Menu {
	
	private Island island;

	private LinkedHashMap<Integer, EnumMap<PlaceHolderType, String>> list = new LinkedHashMap<>();

	public UpgradeMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
		island = ToolsIsland.getIslandOfPlayer(playerMenuUtility.getOwner().getUniqueId());
	}

	@Override
	public String getMenuName() {
		return "Amélioration";
	}

	@Override
	public int getSlots() {
		return 9 * 6;
	}

	@Override
	public boolean cancelAllClicks() {
		return true;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) throws MenuException {

		if(e.getClickedInventory() == null) return;

		if(!list.containsKey(e.getRawSlot())) return;

		EnumMap<PlaceHolderType, String> map = list.get(e.getRawSlot());

		String moneyString = map.getOrDefault(PlaceHolderType.MONEY_REQUIRED, null);
		String expRequired = map.getOrDefault(PlaceHolderType.EXP_REQUIRED, null);
		String levelRequired = map.getOrDefault(PlaceHolderType.LEVEL_XP_REQUIRED, null);
		String islandLevel = map.getOrDefault(PlaceHolderType.ISLAND_LEVEL_REQUIRED, null);

		OfflinePlayer player = Bukkit.getOfflinePlayer(e.getWhoClicked().getUniqueId());

		boolean hasMoney = false;
		boolean hasExp = false;
		boolean hasLevel = false;
		boolean hasIslandLevel = false;

		boolean conditionMoney = moneyString != null && Long.parseLong(moneyString) > 0;
		boolean conditionExp = expRequired != null && Integer.parseInt(expRequired) > 0;
		boolean conditionLevel = levelRequired != null && Integer.parseInt(levelRequired) > 0;
		boolean conditionIsland = islandLevel != null && Integer.parseInt(islandLevel) > 0;

		// Case money
		if(conditionMoney) {
			double balance = Main.getEcon().getBalance(player);
			if(balance >= Long.parseLong(moneyString)) {
				Main.getEcon().withdrawPlayer(player, Double.parseDouble(moneyString));
				hasMoney = true;
			} else {
				Objects.requireNonNull(player.getPlayer()).sendMessage("Pas assez d'argent");
			}
		}
		// Case exp
		if(conditionExp) {
			if(ExperienceUtil.getPlayerExp(Objects.requireNonNull(player.getPlayer())) >= Integer.parseInt(expRequired)) {
				ExperienceUtil.removeExperience(player.getPlayer(), Integer.parseInt(expRequired));
				hasExp= true;
			} else {
				player.getPlayer().sendMessage("Pas assez d'xp");
			}
		}
		// Case level
		if(conditionLevel) {
			if(Objects.requireNonNull(player.getPlayer()).getLevel() >= Integer.parseInt(levelRequired)) {
				ExperienceUtil.removeLevel(player.getPlayer(), Integer.parseInt(levelRequired));
				hasLevel = true;
			} else {
				player.getPlayer().sendMessage("Pas assez de level");
			}
		}
		// Case island level
		if(conditionIsland) {
			if(island.getIslandStats().getLevel() >= Integer.parseInt(islandLevel)) hasIslandLevel = true;
			else Objects.requireNonNull(player.getPlayer()).sendMessage("pas assez niveau ile");
		}

		if((!conditionMoney || hasMoney) && (!conditionLevel || hasLevel) && (!conditionExp || hasExp) && (!conditionIsland || hasIslandLevel)) {
			if(map.containsKey(PlaceHolderType.NEXT_RADIUS))
				island.getIslandStats().setRadiusIsland(Integer.parseInt(map.get(PlaceHolderType.NEXT_RADIUS)));
		}

		ToolsIsland.updateIslandInFile(this.island);
		ToolsIsland.updateIslandVariable(this.island);



	}

	@Override
	public void setMenuItems() {

		setFillerGlass(makeItem(Material.valueOf(Main.getInstance().getConfig().getString("config.menus.filler.icon")),
				MessageUtil.format(Main.getInstance().getConfig().getString("config.menus.filler.name")),
				Main.getInstance().getConfig().getString("config.menus.filler.lore")));
		for(String i : Main.getFiles().get("upgrade").getConfigurationSection("upgrade.island_size").getKeys(false)) {
			ConfigurationSection section = Main.getFiles().get("upgrade").getConfigurationSection("upgrade.island_size."+i);
			int position = section.getInt("position");
			int size = section.getInt("size");
			double money = section.getLong("price.money");
			int exp = section.getInt("price.exp");
			int levelXp = section.getInt("price.level_xp");
			int islandLevel = section.getInt("price.island_level");
			Material icon = Material.valueOf(section.getString("icon"));
			String name = MessageUtil.format(section.getString("name"));
			List<String> description = section.getStringList("description");
			
			EnumMap<PlaceHolderType, String> map = new EnumMap<>(PlaceHolderType.class);
			map.put(PlaceHolderType.MONEY_REQUIRED, money+"");
			map.put(PlaceHolderType.EXP_REQUIRED, exp+"");
			map.put(PlaceHolderType.LEVEL_XP_REQUIRED, levelXp+"");
			map.put(PlaceHolderType.ISLAND_LEVEL_REQUIRED, islandLevel+"");
			map.put(PlaceHolderType.NEXT_RADIUS, size+"");
			map.put(PlaceHolderType.CURRENT_RADIUS_MAX, island.getIslandStats().getRadiusIsland()+"");
			
			ItemBuilder it = new ItemBuilder(icon, 1, name, MessageUtil.format(description, map));
			list.put(position, map);
			inventory.setItem(position, it.toItemStack());

		}
		

	}

}
