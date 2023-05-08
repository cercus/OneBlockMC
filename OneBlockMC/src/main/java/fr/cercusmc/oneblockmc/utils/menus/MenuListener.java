package fr.cercusmc.oneblockmc.utils.menus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import fr.cercusmc.oneblockmc.utils.Logger;
import net.md_5.bungee.api.ChatColor;

public class MenuListener implements Listener {
	
	private Logger logger = new Logger();
	
	@EventHandler
    public void onMenuClick(InventoryClickEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();
        //If the inventoryholder of the inventory clicked on
        // is an instance of Menu, then gg. The reason that
        // an InventoryHolder can be a Menu is because our Menu
        // class implements InventoryHolder!!
        if (holder instanceof Menu menu) {
            if (e.getCurrentItem() == null) { //deal with null exceptions
                return;
            }
           
            if (menu.cancelAllClicks()) {
                e.setCancelled(true); //prevent them from fucking with the inventory
            }

            //Call the handleMenu object which takes the event and processes it
            try {
                menu.handleMenu(e);
            } catch (Exception ex) {
                logger.error(ChatColor.RED + "THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            }
        }

    }

}
