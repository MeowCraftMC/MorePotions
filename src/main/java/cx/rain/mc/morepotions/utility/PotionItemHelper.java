package cx.rain.mc.morepotions.utility;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.api.data.PotionCategory;
import cx.rain.mc.morepotions.api.data.PotionEntry;
import cx.rain.mc.morepotions.api.data.PotionType;
import cx.rain.mc.morepotions.brewing.persistence.CustomPotionData;
import cx.rain.mc.morepotions.brewing.persistence.PotionContainerType;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class PotionItemHelper {
    public static @Nullable NamespacedKey getPotionId(@Nullable ItemStack potion, PotionCategory type) {
        if (potion == null) {
            return null;
        }

        if (!potion.getType().equals(Material.POTION)
                && !potion.getType().equals(Material.SPLASH_POTION)
                && !potion.getType().equals(Material.LINGERING_POTION)) {
            return null;
        }

        if (!potion.hasItemMeta()) {
            return null;
        }

        if (type == PotionCategory.VANILLA) {
            var meta = potion.getItemMeta();
            if (meta instanceof PotionMeta potionMeta && potionMeta.hasBasePotionType()) {
                return Objects.requireNonNull(potionMeta.getBasePotionType()).getKey();
            } else {
                return null;
            }
        } else if (type == PotionCategory.CUSTOM) {
            var dataContainer = potion.getItemMeta().getPersistentDataContainer();
            var data = dataContainer.get(PotionContainerType.KEY_DATA_TYPE, PotionContainerType.INSTANCE);
            if (data == null) {
                return null;
            }
            return data.getCustomPotionId();
        } else {
            return null;
        }
    }

    @NotNull
    public static ItemStack getPotionStack(PotionType type, NamespacedKey potionId, PotionCategory category) {
        var stack = createPotionBottle(type);

        if (category == PotionCategory.VANILLA) {
            var potion = org.bukkit.potion.PotionType.valueOf(potionId.getKey());
            return createVanillaPotion(stack, potion);
        } else if (category == PotionCategory.CUSTOM) {
            var potion = MorePotions.getInstance().getBrewingManager().getPotion(potionId);
            if (potion == null) {
                MorePotions.getInstance().getLogger().warning("Potion '" + potionId + "' was not found!");
                return ItemStack.empty();
            }

            return createCustomPotion(stack, potion);
        } else {
            MorePotions.getInstance().getLogger().warning("Category '" + category + "' was not found!");
            return ItemStack.empty();
        }
    }

    @NotNull
    private static ItemStack createCustomPotion(ItemStack stack, PotionEntry potion) {
        var meta = stack.getItemMeta();
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.setBasePotionType(null);

            potionMeta.clearCustomEffects();
            for (var effect : MorePotions.getInstance().getBrewingManager().getPotionEffects(potion)) {
                potionMeta.addCustomEffect(new PotionEffect(effect.type(), effect.getDuration(), effect.getAmplifier(),
                        effect.byBeacon(), effect.showParticles(), effect.showIcon()), true);
            }

            potionMeta.setColor(potion.getColor());

            if (potion.hasShowName()) {
                potionMeta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNull(potion.getShowName())));
            }

            var dataContainer = potionMeta.getPersistentDataContainer();
            var data = new CustomPotionData();
            data.setCustomPotionId(potion.getId());
            dataContainer.set(PotionContainerType.KEY_DATA_TYPE, PotionContainerType.INSTANCE, data);

            stack.setItemMeta(potionMeta);
            return stack;
        }
        return stack;
    }

    @NotNull
    private static ItemStack createVanillaPotion(ItemStack stack, org.bukkit.potion.PotionType potion) {
        var meta = stack.getItemMeta();
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.setBasePotionType(potion);
            stack.setItemMeta(potionMeta);
            return stack;
        }
        return stack;
    }

    private static ItemStack createPotionBottle(PotionType type) {
        return switch (type) {
            case DRINK -> new ItemStack(Material.POTION);
            case SPLASH -> new ItemStack(Material.SPLASH_POTION);
            case LINGERING -> new ItemStack(Material.LINGERING_POTION);
        };
    }
}
