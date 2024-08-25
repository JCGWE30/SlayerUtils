package slayerutils.slayerutils.Test;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import slayerutils.slayerutils.CustomInventories.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class InnerInventoryTest {

    public InnerInventoryTest(Player p){
        InnerInventory inner = new InnerInventory(7,2,11);
        Template template = new Template()
                .name("test")
                .rows(4)
                .setInnerInventory(inner, CommonSlot.LIGHT_GRAY_BACKGROUND);

        for(int i =0;i<inner.getSize();i++)
            template.addSlot(new Slot()
                    .location(inner.convertToInnerSlot(i))
                    .name("This should be at slot "+i)
                    .type(randomMat()));

        new CustomInventory(template,p);
    }

    private Material randomMat(){
        Random random = new Random();
        List<Material> itemsOnly = Arrays.stream(Material.values()).filter(Material::isItem).collect(Collectors.toList());
        int index = random.nextInt(itemsOnly.size());
        return itemsOnly.get(index);
    }

    public static InventoryBuilder register(){
        return InnerInventoryTest::new;
    }
}
