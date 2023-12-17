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

    public static final int unix24hrs = 86400;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(instance);
        sessionManager = new SessionManager(instance);
        timeManager = new TimeManager(instance);

        configManager.createTimeConfig();

        //must be done AFTER config is loaded
        timeManager.loadTimeMap();

        enableCustomRecipes();

        registerListeners();
        registerCommands();

        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
            w.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
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

    private void enableCustomRecipes() {
        Iterator<Recipe> it = getServer().recipeIterator();
        Recipe recipe;
        while (it.hasNext()) {
            recipe = it.next();
            if (recipe != null) {
                switch (recipe.getResult().getType()) {
                    case BOOKSHELF, TNT, LEATHER_HELMET, IRON_HELMET, GOLDEN_HELMET, DIAMOND_HELMET, NETHERITE_HELMET ->
                            it.remove();
                    default -> {
                    }
                }
            }
        }

        ItemStack tnt = new ItemStack(Material.TNT);
        ItemStack nametag = new ItemStack(Material.NAME_TAG);

        ShapedRecipe tntRecipe = new ShapedRecipe(new NamespacedKey(this, "tntnew"), tnt);
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

    @Override
    public void onDisable() {
        configManager.saveTimeConfig();
    }

    public static ThirdLife getInstance() {
        return instance;
    }
}
