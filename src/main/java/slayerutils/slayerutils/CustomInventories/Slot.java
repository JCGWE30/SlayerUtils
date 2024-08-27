package slayerutils.slayerutils.CustomInventories;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import slayerutils.slayerutils.Utility.InvenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Slot {
    private final Sound defaultSound = Sound.UI_BUTTON_CLICK;
    private final float defaultSoundPitch = 2f;
    private final float defaultSoundVolume = 1f;

    int location = 0;
    String name = "Item";
    List<String> lore = new ArrayList<>();

    Material type = Material.IRON_BLOCK;
    PlayerProfile skullData;

    int amount = 1;

    boolean glint = false;

    SlotClick click = null;
    boolean clickset = false;

    SlotClick rightclick = null;
    boolean rightclickset = false;

    SlotUpdate update = null;

    boolean clickSoundSet = false;
    Sound clickSound = null;
    float soundVolume = defaultSoundVolume;
    float soundPitch = defaultSoundPitch;

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
    protected Slot setLore(List<String> lore){
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
    public Slot skull(Player p){
        this.type = Material.PLAYER_HEAD;
        skullData = p.getPlayerProfile();
        return this;
    }
    public Slot skull(String url){
        this.type = Material.PLAYER_HEAD;
        skullData = InvenUtils.getProfile(url);
        return this;
    }
    private Slot setSkullData(PlayerProfile skullData){
        this.skullData=skullData;
        return this;
    }
    public Slot stripSkull(){
        skullData=null;
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
    public Slot clickSound(Sound sound){
        clickSound = sound;
        clickSoundSet = true;
        return this;
    }
    public Slot clickSound(Sound sound,float soundPitch, float soundVolume){
        clickSound = sound;
        this.soundPitch = soundPitch;
        this.soundVolume = soundVolume;
        clickSoundSet = true;
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
    protected void playClick(Player p){
        if(!clickSoundSet){
            p.playSound(p.getLocation(),defaultSound,defaultSoundVolume,defaultSoundPitch);
        }else if(clickSound!=null){
            p.playSound(p.getLocation(),clickSound,soundVolume,soundPitch);
        }
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
        if(clickSoundSet)
            slot.clickSound(clickSound,soundPitch,soundVolume);
        if(skullData!=null)
            slot.setSkullData(skullData);
        return slot;
    }
}

