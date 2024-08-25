package slayerutils.slayerutils.CustomInventories;

import java.util.ArrayList;
import java.util.List;

public class Template {
    String name = "Inventory";
    int rows = 1;

    boolean background = true;

    boolean autoupdate = false;

    boolean clickupdate = false;

    List<Slot> slots = new ArrayList<>();

    InventoryClose closeAction;

    InnerInventory innerInventory;
    Slot innerInventoryBackground;
    public Template name(String name){
        this.name=name;
        return this;
    }
    public Template rows(int rows){
        this.rows=rows;
        return this;
    }
    public Template noBackground(){
        background=false;
        return this;
    }
    public Template autoupdate(){
        autoupdate=true;
        return this;
    }
    public Template closeAction(InventoryClose cls){
        closeAction=cls;
        return this;
    }
    public Template updateOnClick(){
        clickupdate=true;
        return this;
    }
    public Template addSlot(Slot s){
        slots.add(s);
        return this;
    }

    public Template setInnerInventory(InnerInventory inner, Slot backgroundSlot){
        innerInventory = inner;
        innerInventoryBackground = backgroundSlot;
        return this;
    }
    public Template setInnerInventory(InnerInventory inner, CommonSlot backgroundSlot){
        innerInventory = inner;
        innerInventoryBackground = backgroundSlot.getSlot();
        return this;
    }
    public Template setInnerInventory(InnerInventory inner){
        innerInventory = inner;
        innerInventoryBackground = null;
        return this;
    }
}
