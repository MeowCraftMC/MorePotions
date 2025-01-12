package cx.rain.mc.morepotions.brewing;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.api.data.EffectEntry;
import cx.rain.mc.morepotions.api.data.PotionCategory;
import cx.rain.mc.morepotions.api.data.PotionEntry;
import cx.rain.mc.morepotions.api.data.RecipeEntry;
import cx.rain.mc.morepotions.utility.Pair;
import cx.rain.mc.morepotions.utility.PotionItemHelper;
import cx.rain.mc.morepotions.utility.PotionItemPredicate;
import io.papermc.paper.potion.PotionMix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrewingManager {
    private final Map<NamespacedKey, EffectEntry> effects = new HashMap<>();
    private final Map<NamespacedKey, PotionEntry> potions = new HashMap<>();
    private final Map<NamespacedKey, RecipeEntry> recipes = new HashMap<>();
    private final List<PotionMix> potionMixes = new ArrayList<>();

    public BrewingManager() {
    }

    public void clear() {
        recipes.keySet().forEach(Bukkit.getServer().getPotionBrewer()::removePotionMix);
        recipes.clear();
        potionMixes.clear();
        potions.clear();
        effects.clear();
    }

    public void addEffect(@Nonnull EffectEntry entry) {
        effects.put(entry.id(), entry);
    }

    public void addPotion(@Nonnull PotionEntry entry) {
        potions.put(entry.getId(), entry);
    }

    public void addRecipe(@Nonnull RecipeEntry entry) {
        recipes.put(entry.getId(), entry);
        var potionMix = createPotionMix(entry);
        potionMixes.add(potionMix);
        Bukkit.getServer().getPotionBrewer().addPotionMix(potionMix);
    }

    private PotionMix createPotionMix(RecipeEntry entry) {
        return new PotionMix(entry.getId(),
                PotionItemHelper.getPotionStack(entry.getType(), entry.getResultId(), entry.getResultCategory()),
                PotionMix.createPredicateChoice(new PotionItemPredicate(entry.getBaseCategory(), entry.getBasePotion())),
                new RecipeChoice.MaterialChoice(entry.getIngredient()));
    }

    public @Nullable PotionEntry getPotion(@Nonnull NamespacedKey potionId) {
        return potions.get(potionId);
    }

    public RecipeEntry matchRecipe(ItemStack input, ItemStack ingredient, ItemStack result) {
        for (var p : potionMixes) {
            var r = p.getInput().test(input) && p.getIngredient().test(ingredient) && p.getResult().isSimilar(result);
            if (r) {
                return recipes.get(p.getKey());
            }
        }
        return null;
    }

    public List<EffectEntry> getPotionEffects(PotionEntry potion) {
        var list = new ArrayList<EffectEntry>();

        for (var id : potion.getEffects()) {
            var effect = effects.get(id);

            if (effect != null) {
                list.add(effect);
            } else {
                throw new RuntimeException("Effect '" + id + "' used by '" + potion.getId() + "' was not registered!");
            }
        }

        return list;
    }
}
