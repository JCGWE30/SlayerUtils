package slayerutils.slayerutils.CustomInventories;

import org.bukkit.Material;

import java.util.List;

public enum CommonSlot {
    LIGHT_GRAY_BACKGROUND(" ",null,Material.LIGHT_GRAY_STAINED_GLASS_PANE),
    QUESTION_BEDROCK("§c???",null,Material.BEDROCK);

    private Slot slot;
    CommonSlot(String name, List<String> lore, Material mat){
        slot = new Slot()
                .name(name)
                .setLore(lore)
                .type(mat);
    }

    public Slot getSlot(){
        return slot;
    }
}
