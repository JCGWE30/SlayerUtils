package slayerutils.slayerutils.UtilCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DisablePlugin implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()) return true;
        if(args.length==0) return true;

        String pluginName = args[0];

        Plugin plg = Bukkit.getServer().getPluginManager().getPlugin(pluginName);

        if(plg==null)
            return true;

        Bukkit.getServer().getPluginManager().disablePlugin(plg);

        sender.sendMessage("§aPlugin "+pluginName+" sucessfully disabled");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length!=1) return new ArrayList<>();
        List<String> pluginnames = Arrays.stream(Bukkit.getServer().getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toList());
        return pluginnames;
    }
}
