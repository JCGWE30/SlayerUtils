package slayerutils.slayerutils.CustomInventories;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class CustomInventoryEvents implements Listener {

    HashMap<Player,Long> debounce = new HashMap<>();

    @EventHandler
    private void close(InventoryCloseEvent e){
        if(CustomInventory.getInventory(((Player) e.getPlayer()))==null)
            return;
        CustomInventory.getInventory(((Player) e.getPlayer())).close();
    }

    @EventHandler
    private void click(InventoryClickEvent e){
        Player p = ((Player) e.getWhoClicked());
        CustomInventory inv = CustomInventory.getInventory(p);
        if(inv==null) return;
        e.setCancelled(true);
        long db = debounce.getOrDefault(p,0L);
        if(new Date().getTime()-db<100)
            return;
        debounce.put(p,new Date().getTime());
        Slot slot = inv.getSlot(e.getSlot());
        if(slot==null) return;
        if(e.getClick().isRightClick()&&slot.rightclickset){
            slot.rightclick.onClick(e.getClick());
            slot.playClick(p);
        }else if(slot.clickset) {
            slot.click.onClick(e.getClick());
            slot.playClick(p);
        }
        if(!inv.isOpen())
            return;
        inv.update(true);
    }
}
