package slayerutils.slayerutils.CustomInventories;

import org.bukkit.Material;
import slayerutils.slayerutils.Utility.InvenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Slot {
    int location = 0;
    String name = "Item";
    List<String> lore = new ArrayList<>();
    Material type = Material.IRON_BLOCK;
    int amount = 1;
    boolean glint = false;
    SlotClick click = null;
    boolean clickset = false;
    SlotClick rightclick = null;
    boolean rightclickset = false;
    SlotUpdate update = null;
    public Slot location(int location){
        this.location=location;
        return this;
    }
    public Slot name(String name){
        this.name=name;
        return this;
    }
    public Slot lore(String lore){
        this.lore.add(lore);
        return this;
    }
    private Slot setLore(List<String> lore){
        this.lore=lore;
        return this;
    }
    public Slot splitLore(String lore){
        this.lore= CustomInventory.listString(lore);
        return this;
    }
    public Slot type(Material type){
        this.type=type;
        return this;
    }
    public Slot amount(int amount){
        this.amount=amount;
        return this;
    }
    public Slot shiny(){
        glint=true;
        return this;
    }
    public Slot clickAction(SlotClick sc){
        click=sc;
        clickset=true;
        return this;
    }
    public Slot rightClickAction(SlotClick sc){
        rightclick=sc;
        rightclickset=true;
        return this;
    }
    public Slot removeClickAction(){
        click=null;
        clickset=false;
        return this;
    }
    public Slot updateAction(SlotUpdate su){
        update=su;
        return this;
    }
    @Override
    public Slot clone(){
        Slot slot = new Slot()
                .location(location)
                .name(name)
                .setLore(lore)
                .type(type)
                .amount(amount);
        if(glint)
            slot.shiny();
        if(clickset)
            slot.clickAction(click);
        if(update!=null)
            slot.updateAction(update);
        return slot;
    }
}

