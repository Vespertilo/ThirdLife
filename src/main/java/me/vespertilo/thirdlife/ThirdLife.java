package me.vespertilo.thirdlife;

import me.vespertilo.thirdlife.commands.EndSessionCommand;
import me.vespertilo.thirdlife.commands.RerollBoogeyCommand;
import me.vespertilo.thirdlife.commands.StartSessionCommand;
import me.vespertilo.thirdlife.commands.TimeCommand;
import me.vespertilo.thirdlife.commands.tabcomplete.TabComplete;
import me.vespertilo.thirdlife.config.ConfigManager;
import me.vespertilo.thirdlife.listeners.PersistentListeners;
import me.vespertilo.thirdlife.time.TimeManager;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public final class ThirdLife extends JavaPlugin {

    private static ThirdLife instance;

    public ConfigManager configManager;
    public SessionManager sessionManager;
    public TimeManager timeManager;

    private CustomRecipes customRecipes;

    public static final int unix24hrs = 86400;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(instance);
        sessionManager = new SessionManager(instance);
        timeManager = new TimeManager(instance);

        customRecipes = new CustomRecipes(instance);

        configManager.createTimeConfig();

        //must be done AFTER config is loaded
        timeManager.loadTimeMap();

        customRecipes.disableVanillaCrafts();
        customRecipes.addCustomRecipes();

        registerListeners();
        registerCommands();

        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
            WorldBorder border = w.getWorldBorder();
            border.setSize(700);
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PersistentListeners(instance, sessionManager, timeManager), this);
    }

    private void registerCommands() {
        this.getCommand("startsession").setExecutor(new StartSessionCommand(instance));
        this.getCommand("endsession").setExecutor(new EndSessionCommand(instance));
        this.getCommand("rollboogey").setExecutor(new RerollBoogeyCommand(instance));
        this.getCommand("time").setExecutor(new TimeCommand(instance));

        this.getCommand("time").setTabCompleter(new TabComplete());
    }

    @Override
    public void onDisable() {
        configManager.saveTimeConfig();
    }

    public static ThirdLife getInstance() {
        return instance;
    }
}
