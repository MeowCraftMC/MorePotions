package cx.rain.mc.morepotions.listener;

import cx.rain.mc.morepotions.MorePotions;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import java.util.*;

public class DrinkPotionListener implements Listener {
    private static final Random RAND = new Random();

    private static final List<PotionEffectType> POTIONS = new ArrayList<>();

    public DrinkPotionListener() {
        POTIONS.clear();
        POTIONS.addAll(Registry.POTION_EFFECT_TYPE.stream().toList());
    }

    @EventHandler
    public void onDrinkThickPotion(PlayerItemConsumeEvent event) {
        if (!MorePotions.getInstance().getConfigManager().allowRandomEffect()) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }
        if (!event.getItem().getType().equals(Material.POTION)) {
            return;
        }

        var potion = (PotionMeta)event.getItem().getItemMeta();

        if (!potion.hasBasePotionType()) {
            return;
        }

        if (Objects.requireNonNull(potion.getBasePotionType()).equals(PotionType.THICK)) {
            event.getPlayer().addPotionEffect(getRandomEffect());
        }
    }

    private PotionEffect getRandomEffect() {
        var type = POTIONS.get(RAND.nextInt(0, POTIONS.size()));
        var duration = RAND.nextInt(5, 87) * 20;
        var amplifier = RAND.nextInt(0, 4);
        return new PotionEffect(type, duration, amplifier, false, true, true);
    }
}
