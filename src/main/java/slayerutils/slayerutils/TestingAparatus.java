package slayerutils.slayerutils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Objects;

public class TestingAparatus implements Listener {
    private static final String[] whitelist = {"Slayerutils","ProtocolLib"};

    protected static TestingAparatus instance;

    private boolean testing = false;
    private String testingPlugin = "";

    protected static void load(){
        instance = new TestingAparatus();
        String rawtest = System.getenv("TESTING_APARATUS");

        if(rawtest!=null){
            instance.testing=true;
            instance.testingPlugin = rawtest;

            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                if(!enabled(plugin))
                    Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }

    protected static boolean isTesting(){
        return instance.testing;
    }

    @EventHandler
    private void onPluginLoad(PluginEnableEvent e){
        if(!enabled(e.getPlugin()))
            Bukkit.getPluginManager().disablePlugin(e.getPlugin());
    }

    public static boolean enabled(Plugin plg){
        if(!instance.testing)
            return true;

        if(Arrays.asList(whitelist).contains(plg.getName()))
            return true;

        return Objects.equals(instance.testingPlugin, plg.getName());
    }

}
