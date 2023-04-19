package me.vespertilo.thirdlife;

import me.vespertilo.thirdlife.commands.StartSessionCommand;
import me.vespertilo.thirdlife.config.ConfigHelper;
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
            try {
                timeConfigFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            saveResource("times.yml", false);
        }

        timeConfig = YamlConfiguration.loadConfiguration(timeConfigFile);

    }

    @Override
    public void onDisable() {
        ConfigHelper.setTimeHashmap(getTimeConfig(), timeConfigFile, scoreboardManager.getCachedTime());
    }

    public static ThirdLife getInstance() {
        return instance;
    }
}
