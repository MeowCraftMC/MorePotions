package cx.rain.mc.morepotions.listener;

import cx.rain.mc.morepotions.MorePotions;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ClickDragonEggListener implements Listener {

    @EventHandler
    public void onClickDragonEgg(PlayerInteractEvent event) {
        if (!MorePotions.getInstance().getConfigManager().getDragonBreathByEgg()) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !event.hasBlock() || !event.hasItem()) {
            return;
        }
        if (!Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.DRAGON_EGG)) {
            return;
        }
        if (event.useItemInHand() != Event.Result.DENY) {
            return;
        }
        var stack = Objects.requireNonNull(event.getItem());
        if (!stack.getType().equals(Material.GLASS_BOTTLE)) {
            return;
        }
        event.setCancelled(true);
        stack.subtract();
        event.getPlayer().playSound(Sound.sound()
                .type(org.bukkit.Sound.ITEM_BOTTLE_FILL_DRAGONBREATH.key())
                .source(Sound.Source.PLAYER)
                .build());
        event.getPlayer().getInventory().addItem(new ItemStack(Material.DRAGON_BREATH, 1));
    }
}
