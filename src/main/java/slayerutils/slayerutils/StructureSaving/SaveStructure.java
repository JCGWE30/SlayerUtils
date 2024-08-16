package slayerutils.slayerutils.StructureSaving;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
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
import java.util.List;
import java.util.Objects;

public class SaveStructure implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp() || !(sender instanceof Player)) return true;
        Player p = ((Player) sender);
        if(args.length==0) return true;
        StructureEvents.saveStructure(p,args[0]);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length!=1) return new ArrayList<>();
        List<String> strs = new ArrayList<>();
        String pathlocation = Slayerutils.getThe().getDataFolder().toString()+"/structures";
        Path pt = Paths.get(pathlocation);
        try {
            Files.list(pt).forEach(f-> strs.add(f.getFileName().toString().split(".json")[0]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strs;
    }
}
