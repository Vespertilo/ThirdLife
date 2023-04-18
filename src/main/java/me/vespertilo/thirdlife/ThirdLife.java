package me.vespertilo.thirdlife;

import me.vespertilo.thirdlife.commands.ConfigCommand;
import me.vespertilo.thirdlife.commands.StartSessionCommand;
import me.vespertilo.thirdlife.config.TimeConfig;
import me.vespertilo.thirdlife.listeners.PersistentListeners;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mikigal.config.ConfigAPI;
import pl.mikigal.config.style.CommentStyle;
import pl.mikigal.config.style.NameStyle;

public final class ThirdLife extends JavaPlugin {

    private static ThirdLife instance;

    public TimeConfig timeConfig;

    public static final int unix24hrs = 86400;

    @Override
    public void onEnable() {
        instance = this;

        timeConfig = ConfigAPI.init(
                TimeConfig.class,
                NameStyle.UNDERSCORE,
                CommentStyle.INLINE,
                true,
                this
        );

        registerListeners();
        registerCommands();

        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PersistentListeners(instance), this);
    }

    private void registerCommands() {
        this.getCommand("startsession").setExecutor(new StartSessionCommand());
        this.getCommand("config").setExecutor(new ConfigCommand(timeConfig));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ThirdLife getInstance() {
        return instance;
    }
}
