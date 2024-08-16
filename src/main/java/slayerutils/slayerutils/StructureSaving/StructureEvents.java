package slayerutils.slayerutils.StructureSaving;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import slayerutils.slayerutils.SlayerJson.SlayerJson;
import slayerutils.slayerutils.Slayerutils;

import java.util.*;

public class StructureEvents implements Listener {
    private static HashMap<Player, List<Block>> playerBlockLists = new HashMap<>();
    private static HashMap<Player,DisplayData> originBlocks = new HashMap<>();


    private static HashMap<Player, List<Location>> playerCorners = new HashMap<>();

    public class DisplayData{
        public Location location;
        public Entity blockDisplay;

        public DisplayData loc(Location b){
            location =b;
            return this;
        }

        public DisplayData display(Entity e){
            blockDisplay=e;
            return this;
        }
    }
    @EventHandler
    private void placeBlock(BlockPlaceEvent e){
        if(ToggleCommand.isSaving(e.getPlayer())) {
            List<Block> blocklist = getBlockList(e.getPlayer());
            blocklist.add(e.getBlock());
            playerBlockLists.put(e.getPlayer(),blocklist);

            if(!originBlocks.containsKey(e.getPlayer())){
                BlockDisplay display = ((BlockDisplay) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.BLOCK_DISPLAY));
                display.setBlock(e.getBlock().getBlockData());
                display.setBrightness(new Display.Brightness(15,15));
                display.setDisplayHeight(0.5f);
                display.setGlowing(true);
                originBlocks.put(e.getPlayer(),new DisplayData().loc(e.getBlock().getLocation()).display(display));
            }
        }
    }

    public static void saveStructure(Player p, String name){
        StructureEvents.DisplayData data = originBlocks.getOrDefault(p, null);

        if(data==null)
            return;

        Location originLoc = data.location;
        p.sendMessage("§aSaving structure as file "+name+".json");
        List<SlayerJson> jsons = new ArrayList<>();
        for(Block b:getBlockList(p)){
            List<Integer> locs = deductOrigin(originLoc, b.getLocation());
            SlayerJson blockinfo = new SlayerJson()
                    .set("block",b.getType().toString())
                    .set("x",locs.get(0))
                    .set("y",locs.get(1))
                    .set("z",locs.get(2));
            if(b.getBlockData() instanceof Directional){
                Directional dir = ((Directional) b.getBlockData());
                dir.getFacing();
                blockinfo.set("direction",dir.getFacing().toString());
            }
            jsons.add(blockinfo);
        }
        SlayerJson json = new SlayerJson();
        json.setJsonList("blocks",jsons);
        json.set("origin",new SlayerJson()
                .set("x",originLoc.getBlockX())
                .set("y",originLoc.getBlockY())
                .set("z",originLoc.getBlockZ()));
        json.saveToFile(Slayerutils.getThe(),"structures/"+name);
        System.out.println(json.stringify());
        ToggleCommand.disable(p);
    }

    @EventHandler
    private void breakBlock(BlockBreakEvent e){
        if(ToggleCommand.isSaving(e.getPlayer())) {
            List<Block> blocklist = getBlockList(e.getPlayer());
            blocklist.remove(e.getBlock());
            playerBlockLists.put(e.getPlayer(),blocklist);

            DisplayData data = originBlocks.getOrDefault(e.getPlayer(), null);

            if(data==null)
                return;

            if(Objects.equals(data.location, e.getBlock().getLocation())){
                originBlocks.get(e.getPlayer()).blockDisplay.remove();
                originBlocks.remove(e.getPlayer());
            }
        }
        if(CornerToggleCommand.isSaving(e.getPlayer())){
            e.setCancelled(true);
            List<Location> locs = getCornerList(e.getPlayer());
            if(locs.isEmpty()){
                e.getPlayer().sendMessage("§aPosition 1 Saved");
                locs.add(e.getBlock().getLocation());
            }else{
                locs.add(e.getBlock().getLocation());
                e.getPlayer().sendMessage("§aPosition 2 Saved. Adding blocks");
                Bukkit.getScheduler().runTask(Slayerutils.getThe(),() ->{
                    int startingx = Math.min(locs.get(0).getBlockX(),locs.get(1).getBlockX());
                    int startingy = Math.min(locs.get(0).getBlockY(),locs.get(1).getBlockY());
                    int startingz = Math.min(locs.get(0).getBlockZ(),locs.get(1).getBlockZ());


                    int endingx = Math.max(locs.get(0).getBlockX(),locs.get(1).getBlockX());
                    int endingy = Math.max(locs.get(0).getBlockY(),locs.get(1).getBlockY());
                    int endingz = Math.max(locs.get(0).getBlockZ(),locs.get(1).getBlockZ());

                    System.out.println("X: "+startingx+" "+endingx);
                    System.out.println("Y: "+startingy+" "+endingy);
                    System.out.println("Z: "+startingz+" "+endingz);
                    List<Block> blocklist = getBlockList(e.getPlayer());
                    for(int x=startingx;x<endingx+1;x++){
                        for(int y=startingy;y<endingy+1;y++){
                            for(int z = startingz; z<endingz+1; z++){
                                Location loc = new Location(locs.get(0).getWorld(),x,y,z);
                                if(loc.getBlock().getType()!= Material.AIR){
                                    blocklist.add(loc.getBlock());
                                }
                            }
                        }
                    }
                    playerBlockLists.put(e.getPlayer(),blocklist);
                    ToggleCommand.enable(e.getPlayer());
                    e.getPlayer().sendMessage("§aSelection saved containing a total of "+blocklist.size());
                    locs.clear();
                });
                CornerToggleCommand.disable(e.getPlayer());
            }
            playerCorners.put(e.getPlayer(),locs);
        }

    }

    private static List<Block> getBlockList(Player p){
        return playerBlockLists.getOrDefault(p,new ArrayList<>());
    }

    private List<Location> getCornerList(Player p){
        return playerCorners.getOrDefault(p,new ArrayList<>());
    }

    public static List<Block> getTrackedBlocks(Player p){
        return playerBlockLists.getOrDefault(p,new ArrayList<>());
    }

    public static void clearTrackedBlocks(Player p){
        playerBlockLists.remove(p);

        DisplayData data = originBlocks.getOrDefault(p,null);
        if(data==null)
            return;
        data.blockDisplay.remove();
        originBlocks.remove(p);
    }

    public static void setTrackedBlocks(Player p, List<Block> blocks){
        playerBlockLists.put(p,blocks);
    }

    private static List<Integer> deductOrigin(Location origin,Location position){
        Location finalLocation = position.subtract(origin);
        return Arrays.asList(
                finalLocation.getBlockX(),
                finalLocation.getBlockY(),
                finalLocation.getBlockZ()
        );
    }
}
