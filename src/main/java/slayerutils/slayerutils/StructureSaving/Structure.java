package slayerutils.slayerutils.StructureSaving;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import slayerutils.slayerutils.SlayerJson.SlayerJson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Structure {
    private SlayerJson json;
    private Location origin;
    public Structure(SlayerJson structureJson, World w){
        json=structureJson;
        SlayerJson originjson = json.getSub("origin");
        origin = new Location(w,originjson.getInt("x"),originjson.getInt("y"),originjson.getInt("z"));
    }
    public Structure(SlayerJson structureJson, Location origin){
        json=structureJson;
        this.origin=origin.clone();
    }

    public List<Block> load(){
        return json.getJsonList("blocks")
                .stream()
                .map((b)->setBlock(b,origin))
                .collect(Collectors.toList());
    }

    public void unload(){
        json.getJsonList("blocks").forEach(b->removeBlock(b,origin));
    }

    private Block setBlock(SlayerJson json,Location origin){
        Location loc = new Location(origin.getWorld(),json.getInt("x"),json.getInt("y"),json.getInt("z"));
        loc=loc.add(origin);
        loc.getBlock().setType(Material.valueOf(json.get("block")),false);
        if(json.getKeys().contains("direction")){
            Directional dir = ((Directional) loc.getBlock().getBlockData());
            dir.setFacing(BlockFace.valueOf(json.get("direction")));
            loc.getBlock().setBlockData(dir);
        }
        return loc.getBlock();
    }

    private void removeBlock(SlayerJson json,Location origin){
        Location loc = new Location(origin.getWorld(),json.getInt("x"),json.getInt("y"),json.getInt("z"));
        loc=loc.add(origin);
        loc.getBlock().setType(Material.AIR,false);
    }
}
