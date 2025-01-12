package cx.rain.mc.morepotions.api.event;

import cx.rain.mc.morepotions.api.data.RecipeEntry;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.BrewerInventory;
import org.jetbrains.annotations.NotNull;

public class MorePotionBrewEvent extends BlockEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BrewingStand brewingStand;
    private final BrewerInventory inventory;
    private final RecipeEntry recipe;

    public MorePotionBrewEvent(@NotNull Block theBlock, @NotNull BrewingStand brewingStand, @NotNull BrewerInventory inventory, @NotNull RecipeEntry recipe) {
        super(theBlock);
        this.brewingStand = brewingStand;
        this.inventory = inventory;
        this.recipe = recipe;
    }

    public BrewingStand getBrewingStand() {
        return brewingStand;
    }

    public BrewerInventory getInventory() {
        return inventory;
    }

    public RecipeEntry getRecipe() {
        return recipe;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
