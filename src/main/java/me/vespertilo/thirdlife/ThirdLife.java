package me.vespertilo.thirdlife;

import me.vespertilo.thirdlife.commands.StartSessionCommand;
import me.vespertilo.thirdlife.config.ConfigHelper;
import me.vespertilo.thirdlife.listeners.PersistentListeners;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

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

        enableCustomRecipes();

        registerListeners();
        registerCommands();

        sessionManager = new SessionManager(instance);
        scoreboardManager = new ScoreboardManager(instance);

        HashMap<UUID, Integer> times = ConfigHelper.getTimeHashmap(instance.getTimeConfig());
        scoreboardManager.setCachedTime(times);

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

    private void enableCustomRecipes() {
        Iterator<Recipe> it = getServer().recipeIterator();
        Recipe recipe;
        while (it.hasNext()) {
            recipe = it.next();
            if (recipe != null) {
                switch (recipe.getResult().getType()) {
                    case BOOKSHELF, TNT -> it.remove();
                    default -> {
                    }
                }
            }
        }

        ItemStack tnt = new ItemStack(Material.TNT);
        ItemStack nametag = new ItemStack(Material.NAME_TAG);

        ShapedRecipe tntRecipe = new ShapedRecipe(new NamespacedKey(this, "tnt"), tnt);
        tntRecipe.shape("PSP", "SGS", "PSP");
        tntRecipe.setIngredient('P', Material.PAPER);
        tntRecipe.setIngredient('S', Material.SAND);
        tntRecipe.setIngredient('G', Material.GUNPOWDER);

        ShapedRecipe nametagRecipe = new ShapedRecipe(new NamespacedKey(this, "nametagnew"), nametag);
        nametagRecipe.shape("   ", " S ", " P ");
        nametagRecipe.setIngredient('S', Material.STRING);
        nametagRecipe.setIngredient('P', Material.PAPER);

        Bukkit.addRecipe(tntRecipe);
        Bukkit.addRecipe(nametagRecipe);


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
