package slayerutils.slayerutils.StructureSaving;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()) return true;
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if(LoadCommand.playerStructures.containsKey(p)){
                LoadCommand.playerStructures.get(p).unload();
                LoadCommand.playerStructures.remove(p);
                ToggleCommand.disable(p);

            }
        }else{
            if(LoadCommand.consoleStructure!=null){
                LoadCommand.consoleStructure.unload();
                LoadCommand.consoleStructure=null;
            }
        }
        return true;
    }
}
