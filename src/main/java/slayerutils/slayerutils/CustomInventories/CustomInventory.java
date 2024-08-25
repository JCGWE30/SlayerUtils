package slayerutils.slayerutils.CustomInventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import slayerutils.slayerutils.Slayerutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomInventory {
    private static HashMap<String,InventoryBuilder> builders = new HashMap<>();
    private static HashMap<Player,CustomInventory> inventories = new HashMap<>();

    private Template template;
    private HashMap<SlotClick, Slot> clicks = new HashMap<>();
    private HashMap<SlotUpdate,Slot> updates = new HashMap<>();
    private CustomInventoryHolder holder = null;
    private Player viewer;
    public CustomInventory(Template template,Player p){
        this.template=template;
        viewer=p;
        CustomInventoryHolder cih = new CustomInventoryHolder(template);
        CustomInventory thisinv = this;
        if(template.autoupdate){
            new BukkitRunnable(){

                @Override
                public void run() {
                    if(getInventory(p)!=thisinv){
                        this.cancel();
                        return;
                    }
                    thisinv.update(false);
                }
            }.runTaskTimer(JavaPlugin.getPlugin(Slayerutils.class),10,10);
        }
        viewer.openInventory(cih.getInventory());
        inventories.put(viewer,this);
    }

    public static InventoryBuilder getById(String id){
        if(!builders.containsKey(id))
            return null;
        return builders.get(id);
    }

    public static void register(String id, InventoryBuilder ib){
        builders.put(id,ib);
    }

    public static CustomInventory getInventory(Player p){
        if(!inventories.containsKey(p))
            return null;
        return inventories.get(p);
    }

    public Slot getSlot(int location){
        for(Slot s: template.slots){
            if(s.location==location)
                return s;
        }
        return null;
    }

    private void setSlot(int location, Slot slot){
        int counter = 0;
        for(Slot s:template.slots){
            if(s.location==location) {
                template.slots.set(counter,slot);
                return;
            }
            counter++;
        }
    }

    public void update(boolean fromclick){
        if(fromclick&&!template.clickupdate)
            return;
        for(int i = 0;i<template.rows*9;i++){
            if(getSlot(i)!=null){
                if(getSlot(i).update!=null) {
                    Slot newslot = getSlot(i).update.update(getSlot(i));
                    ItemStack item = new ItemStack(newslot.type,newslot.amount);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("§r§f"+newslot.name);
                    meta.setLore(newslot.lore);
                    meta.addItemFlags(ItemFlag.values());
                    if(newslot.glint)
                        meta.addEnchant(Enchantment.PROTECTION,1,false);
                    item.setItemMeta(meta);
                    setSlot(i,newslot);
                    int location = newslot.location == -1 ? i : newslot.location;
                    viewer.getOpenInventory().getTopInventory().setItem(location,item);
                }
            }
        }
    }

    public void close(){
        inventories.remove(viewer);
        viewer.closeInventory();
        if(template.closeAction!=null)
            template.closeAction.close();
    }

    public boolean isOpen(){
        return inventories.containsKey(viewer);
    }

    public static class CustomInventoryHolder implements InventoryHolder{

        Inventory inventory;

        public CustomInventoryHolder(Template ci){
            inventory = Bukkit.createInventory(this,ci.rows*9,ci.name);
            if(ci.background) {
                ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE,1);
                ItemMeta pmeta = pane.getItemMeta();
                pmeta.setDisplayName(" ");
                pane.setItemMeta(pmeta);
                for (int i = 0; i < inventory.getSize(); i++) {
                    if(ci.innerInventory!=null){
                        if(ci.innerInventory.inInner(i)){
                            inventory.setItem(i, slotToItem(ci.innerInventoryBackground));
                            continue;
                        }
                    }
                    inventory.setItem(i, pane);
                }
            }
            ci.slots.forEach((slot) ->{
                ItemStack item = slotToItem(slot);
                inventory.setItem(slot.location,item);
            });
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }
    public static List<String> listString(String string){
        List<String> lore = new ArrayList<>();
        String[] strings = string.split(" ");
        int count = 0;
        StringBuilder sb = new StringBuilder();
        //Keep adding new words until its X characters long. Then essensially return and continue
        for(String str:strings){
            sb.append(str).append(" ");
            count+=str.length();
            if(count>=20){
                count=0;
                lore.add("§7"+sb.toString().trim());
                sb=new StringBuilder();
            }
        }
        if(sb.length()>0)
            lore.add("§7"+sb.toString().trim());
        return lore;
    }

    private static ItemStack slotToItem(Slot slot){
        ItemStack item = new ItemStack(slot.type,slot.amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r§f"+slot.name);
        meta.setLore(slot.lore);
        meta.addItemFlags(ItemFlag.values());
        if(slot.glint)
            meta.addEnchant(Enchantment.PROTECTION,1,false);
        item.setItemMeta(meta);
        return item;
    }

    public static int getLocationOf(int x,int y){
        return (y*9)+x;
    }
}
