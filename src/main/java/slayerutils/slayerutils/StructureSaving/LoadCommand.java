package slayerutils.slayerutils.StructureSaving;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import slayerutils.slayerutils.SlayerJson.SlayerJson;
import slayerutils.slayerutils.Slayerutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadCommand implements CommandExecutor, TabCompleter {
    public static HashMap<Player, Structure> playerStructures = new HashMap<>();
    public static Structure consoleStructure;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;
        if(sender instanceof Player){
            Player p = ((Player) sender);
            Structure struct = new Structure(
                    SlayerJson.loadFromFile(Slayerutils.getThe(),"structures/"+args[0]),
                    p.getLocation());
            playerStructures.put(p,struct);
            ToggleCommand.enable(p);
            StructureEvents.setTrackedBlocks(p,struct.load());
        }else{
            consoleStructure = new Structure(
                    SlayerJson.loadFromFile(Slayerutils.getThe(),"structures/"+args[0]),
                    Bukkit.getWorld("world"));
            consoleStructure.load();
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length!=1) return new ArrayList<>();
        List<String> strs = new ArrayList<>();
        String pathlocation = Slayerutils.getThe().getDataFolder()+"/structures";
        Path pt = Paths.get(pathlocation);
        try {
            Files.list(pt).forEach(f-> strs.add(f.getFileName().toString().split(".json")[0]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strs;
    }
}
