package slayerutils.slayerutils.Utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class InvenUtils {
    public static PlayerProfile getProfile(String url){
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try{
            urlObject = new URL(url);
        }catch (MalformedURLException ex) { throw new RuntimeException("Invalid Skin URL", ex);}
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

    public static boolean removeItemsIfPresent(Player p, Material mat,int amount){
        int found = 0;
        for(ItemStack i:p.getInventory().getContents()){
            if(i==null)
                continue;
            if(i.getType()==mat)
                found+=i.getAmount();
        }
        if(found<amount)
            return false;
        found=amount;
        for(int counter=0;counter<p.getInventory().getContents().length;counter++){
            ItemStack i = p.getInventory().getItem(counter);
            if(i==null)
                continue;
            if(i.getType()!=mat)
                continue;
            System.out.println(found+"  "+i.getAmount());
            if(i.getAmount()<=found){
                p.getInventory().setItem(counter,new ItemStack(Material.AIR,1));
                found-=i.getAmount();
            }else{
                System.out.println(i.getAmount()+" "+(i.getAmount()-found));
                i.setAmount(i.getAmount()-found);
                found=0;
            }
            p.updateInventory();
            if(found==0)
                return true;
        }
        return true;
    }
}
