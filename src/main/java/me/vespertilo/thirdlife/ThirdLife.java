package me.vespertilo.thirdlife;

import me.vespertilo.thirdlife.commands.StartSessionCommand;
import me.vespertilo.thirdlife.listeners.PersistentListeners;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ThirdLife extends JavaPlugin {

    private static ThirdLife instance;

    public SessionManager sessionManager;
    public ScoreboardManager scoreboardManager;

    private File timeConfigFile;
    private FileConfiguration timeConfig;

    public static final int unix24hrs = 86400;

    @Override
    public void onEnable() {
        instance = this;

        createTimeConfig();

        registerListeners();
        registerCommands();

        sessionManager = new SessionManager(instance);
        scoreboardManager = new ScoreboardManager(instance);

        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
            w.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            WorldBorder border = w.getWorldBorder();
            border.setSize(700);
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PersistentListeners(instance), this);
    }

    private void registerCommands() {
        this.getCommand("startsession").setExecutor(new StartSessionCommand(instance));
    }

    public FileConfiguration getTimeConfig() {
        return timeConfig;
    }

    private void createTimeConfig() {
        timeConfigFile = new File(getDataFolder(), "times.yml");
        if (!timeConfigFile.exists()) {
            timeConfigFile.getParentFile().mkdirs();
            saveResource("times.yml", false);
        }

        timeConfig = new YamlConfiguration();
        try {
            timeConfig.load(timeConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        /* User Edit:
            Instead of the above Try/Catch, you can also use
            YamlConfiguration.loadConfiguration(customConfigFile)
        */
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ThirdLife getInstance() {
        return instance;
    }
}
