package slayerutils.slayerutils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import slayerutils.slayerutils.CustomInventories.CustomInventory;

public class OpenInventory implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            CustomInventory.getById(args[0]).build(p);
        }
        return true;
    }
}
