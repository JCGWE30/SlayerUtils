package slayerutils.slayerutils;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import slayerutils.slayerutils.CustomInventories.CustomInventory;
import slayerutils.slayerutils.CustomInventories.CustomInventoryEvents;
import slayerutils.slayerutils.StructureSaving.*;
import slayerutils.slayerutils.Test.CoolInventory;
import slayerutils.slayerutils.Test.InnerInventoryTest;
import slayerutils.slayerutils.Test.JSONTest;
import slayerutils.slayerutils.UtilCommands.DisablePlugin;

public final class Slayerutils extends JavaPlugin {

    @Override
    public void onEnable() {
        CustomInventory.register("cool", CoolInventory.register());
        CustomInventory.register("inner", InnerInventoryTest.register());
        getServer().getPluginManager().registerEvents(new CustomInventoryEvents(),this);
        getServer().getPluginManager().registerEvents(new StructureEvents(),this);
        getCommand("openinventory").setExecutor(new OpenInventory());
        getCommand("togglestructuresaving").setExecutor(new ToggleCommand());
        getCommand("togglecornersaving").setExecutor(new CornerToggleCommand());
        getCommand("loadstructure").setExecutor(new LoadCommand());
        getCommand("loadstructure").setTabCompleter(new LoadCommand());
        getCommand("unloadstructure").setExecutor(new UnloadCommand());
        getCommand("savestructure").setExecutor(new SaveStructure());
        getCommand("savestructure").setTabCompleter(new SaveStructure());
        getCommand("disableplugin").setExecutor(new DisablePlugin());
        getCommand("disableplugin").setTabCompleter(new DisablePlugin());
        JSONTest.run();
    }

    public static Plugin getThe(){
        return Slayerutils.getPlugin(Slayerutils.class);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
