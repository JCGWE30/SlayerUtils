package slayerutils.slayerutils.StructureSaving;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CornerToggleCommand implements CommandExecutor {
    private static HashMap<Player, Boolean> playerToggles = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp() || !(sender instanceof Player)) return true;
        Player p = ((Player) sender);
        if(!playerToggles.containsKey(p) || !playerToggles.get(p)){
            playerToggles.put(p,true);
            p.sendMessage("§aCorner mode activated, Punch corner 1");
            ToggleCommand.disable(p);
        }else{
            playerToggles.put(p,false);
            p.sendMessage("§cNo Longer in corner mode");
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
    }
}
