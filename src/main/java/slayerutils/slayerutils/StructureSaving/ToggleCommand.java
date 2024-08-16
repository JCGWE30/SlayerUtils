package slayerutils.slayerutils.StructureSaving;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import slayerutils.slayerutils.Slayerutils;

import java.util.HashMap;

public class ToggleCommand implements CommandExecutor {
    private static HashMap<Player, Boolean> playerToggles = new HashMap<>();
    private static HashMap<Player, BukkitTask> actionbarTasks = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp() || !(sender instanceof Player)) return true;
        Player p = ((Player) sender);
        if(!playerToggles.containsKey(p) || !playerToggles.get(p)){
            playerToggles.put(p,true);
            p.sendMessage("§aSaving Structure");
            CornerToggleCommand.disable(p);
            startActionBar(p);
        }else{
            playerToggles.put(p,false);
            p.sendMessage("§cNo Longer Saving Structure");
            endActionBar(p);
        }
        return true;
    }

    public static boolean isSaving(Player p){
        return playerToggles.getOrDefault(p,false);
    }

    public static void disable(Player p){
        if(!isSaving(p))
            return;
        playerToggles.put(p,false);
        endActionBar(p);
    }

    public static void enable(Player p){
        if(isSaving(p))
            return;
        playerToggles.put(p,true);
        startActionBar(p);
    }

    private static void startActionBar(Player p){
        actionbarTasks.put(p, new BukkitRunnable() {
            @Override
            public void run() {
                p.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacy("§3"+StructureEvents.getTrackedBlocks(p).size()+" Blocks Tracked")
                );
            }
        }.runTaskTimer(Slayerutils.getThe(),1L,10L));
    }

    private static void endActionBar(Player p){
        StructureEvents.clearTrackedBlocks(p);
        if(actionbarTasks.containsKey(p)){
            actionbarTasks.get(p).cancel();
        }
    }
}