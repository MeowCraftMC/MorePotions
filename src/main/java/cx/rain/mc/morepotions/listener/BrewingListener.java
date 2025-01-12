package cx.rain.mc.morepotions.listener;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.api.event.MorePotionBrewEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;

public class BrewingListener implements Listener {
    @EventHandler
    public void onBrew(BrewEvent event) {
        var inventory = event.getContents();
        var block = inventory.getHolder();
        var results = event.getResults();

        for (var i = 0; i <= 2; i++) {
            var input = inventory.getItem(i);
            var ingredient = inventory.getIngredient();
            var result = results.get(i);

            var recipe = MorePotions.getInstance().getBrewingManager().matchRecipe(input, ingredient, result);
            if (recipe != null) {
                if (block != null) {
                    var e = new MorePotionBrewEvent(block.getBlock(), block, inventory, recipe);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                }
            }
        }
    }
}
