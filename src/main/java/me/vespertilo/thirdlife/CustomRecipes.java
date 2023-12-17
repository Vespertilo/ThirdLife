package me.vespertilo.thirdlife;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

public class CustomRecipes {
    private final ThirdLife instance;

    public CustomRecipes(ThirdLife instance) {
        this.instance = instance;
    }

    public void addCustomRecipes() {
        ItemStack tnt = new ItemStack(Material.TNT);
        ItemStack nametag = new ItemStack(Material.NAME_TAG);
        ItemStack saddle = new ItemStack(Material.SADDLE);

        ShapedRecipe tntRecipe = new ShapedRecipe(new NamespacedKey(instance, "tntnew"), tnt);
        tntRecipe.shape("PSP", "SGS", "PSP");
        tntRecipe.setIngredient('P', Material.PAPER);
        tntRecipe.setIngredient('S', Material.SAND);
        tntRecipe.setIngredient('G', Material.GUNPOWDER);

        ShapedRecipe nametagRecipe = new ShapedRecipe(new NamespacedKey(instance, "nametagnew"), nametag);
        nametagRecipe.shape("   ", " S ", " P ");
        nametagRecipe.setIngredient('S', Material.STRING);
        nametagRecipe.setIngredient('P', Material.PAPER);

        ShapedRecipe saddleRecipe = new ShapedRecipe(new NamespacedKey(instance, "saddle"), saddle);
        saddleRecipe.shape("LLL", "L L", "   ");
        saddleRecipe.setIngredient('L', Material.LEATHER);

        Bukkit.addRecipe(tntRecipe);
        Bukkit.addRecipe(nametagRecipe);
        Bukkit.addRecipe(saddleRecipe);
    }

    public void disableVanillaCrafts() {
        Iterator<Recipe> it = instance.getServer().recipeIterator();
        Recipe recipe;
        while (it.hasNext()) {
            recipe = it.next();
            if (recipe != null) {
                switch (recipe.getResult().getType()) {
                    case BOOKSHELF, TNT, IRON_HELMET, LEATHER_HELMET, GOLDEN_HELMET, DIAMOND_HELMET, NETHERITE_HELMET ->
                            it.remove();
                    default -> {
                    }
                }
            }
        }
    }
}
