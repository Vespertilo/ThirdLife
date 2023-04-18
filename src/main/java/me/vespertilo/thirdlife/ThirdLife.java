package me.vespertilo.thirdlife;

import me.vespertilo.thirdlife.listeners.PersistentListeners;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThirdLife extends JavaPlugin {

    private static ThirdLife instance;

    @Override
    public void onEnable() {
        instance = this;


        registerListeners();

        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PersistentListeners(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ThirdLife getInstance() {
        return instance;
    }
}
