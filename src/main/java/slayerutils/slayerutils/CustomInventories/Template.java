package slayerutils.slayerutils.CustomInventories;

import java.util.ArrayList;
import java.util.List;

public class Template {
    String id = "unknown";
    String name = "Inventory";
    int rows = 1;
    boolean background = true;
    boolean autoupdate = false;
    boolean clickupdate = false;
    List<Slot> slots = new ArrayList<>();
    InventoryClose closeAction;
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
}
