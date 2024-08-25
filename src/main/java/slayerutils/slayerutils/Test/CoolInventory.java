package slayerutils.slayerutils.Test;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import slayerutils.slayerutils.CustomInventories.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import static slayerutils.slayerutils.CustomInventories.CustomInventory.getLocationOf;

public class CoolInventory {
    private long starttime = 0;
    public CoolInventory(Player p){
        Template template = new Template()
                .name("Really COOL Inventory")
                .rows(6)
                .autoupdate()
                .updateOnClick();

        template.addSlot(new Slot()
                .location(getLocationOf(4,2))
                .name("Hey there "+p.getName())
                .splitLore("You have had this menu open for 0 seconds")
                .type(Material.DIAMOND)
                .amount(1)

                .clickAction((click) -> {
                    if(TimeUnit.MILLISECONDS.toSeconds(((new Date().getTime())-starttime))>=5){
                        starttime=new Date().getTime();
                        p.sendMessage("§eOrb Lapped");
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1f,1f);
                    }else{
                        p.sendMessage("§cCant lap yet!");
                        p.playSound(p.getLocation(),Sound.ENTITY_VILLAGER_NO,1f,1f);
                    }
                })

                .updateAction((slot) ->
                        slot.splitLore(String.format("You have had this menu Open for %s seconds",TimeUnit.MILLISECONDS.toSeconds(((new Date().getTime())-starttime))))
                                .amount(Math.max(1,(int)TimeUnit.MILLISECONDS.toSeconds(((new Date().getTime())-starttime)))))
        );
        starttime = new Date().getTime();
        new CustomInventory(template,p);
    }
    public static InventoryBuilder register(){
        return CoolInventory::new;
    }
}
