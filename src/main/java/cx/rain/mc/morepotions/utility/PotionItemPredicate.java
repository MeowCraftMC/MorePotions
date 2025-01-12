package cx.rain.mc.morepotions.utility;

import cx.rain.mc.morepotions.api.data.PotionCategory;
import cx.rain.mc.morepotions.brewing.persistence.PotionContainerType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public class PotionItemPredicate implements Predicate<ItemStack> {
    private final PotionCategory potionCategory;
    private final NamespacedKey potionId;

    public PotionItemPredicate(PotionCategory potionCategory, NamespacedKey potionId) {
        this.potionCategory = potionCategory;
        this.potionId = potionId;
    }

    @Override
    public boolean test(@NotNull ItemStack stack) {
        if (!stack.hasItemMeta()) {
            return false;
        }
        var meta = stack.getItemMeta();

        if (potionCategory == PotionCategory.VANILLA) {
            if (meta instanceof PotionMeta potion) {
                if (!potion.hasBasePotionType()) {
                    return false;
                }

                return potionId.equals(Objects.requireNonNull(potion.getBasePotionType()).getKey());
            }
            return false;
        }

        if (potionCategory == PotionCategory.CUSTOM) {
            var data = meta.getPersistentDataContainer().get(PotionContainerType.KEY_DATA_TYPE, PotionContainerType.INSTANCE);
            if (data == null) {
                return false;
            }
            return potionId.equals(data.getCustomPotionId());
        }

        return false;
    }
}
