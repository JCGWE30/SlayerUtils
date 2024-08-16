package slayerutils.slayerutils.QuickRegistering;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class QuickRegistering {
    public static void registerEvent(Plugin plg, Listener lst){
        plg.getServer().getPluginManager().registerEvents(lst,plg);
    }
}
