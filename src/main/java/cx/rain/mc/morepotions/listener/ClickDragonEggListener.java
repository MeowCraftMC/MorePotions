package cx.rain.mc.morepotions.listener;

import cx.rain.mc.morepotions.MorePotions;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ClickDragonEggListener implements Listener {
    private final Plugin plugin;

    public ClickDragonEggListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnClickDragonEgg(PlayerInteractEvent event) {
        if (!MorePotions.getInstance().getConfigManager().getDragonBreathByEgg())
            return;
        if (event.isCancelled())
            return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if (!event.getClickedBlock().getType().equals(Material.DRAGON_EGG))
            return;
        if (!event.getPlayer().getItemInHand().getType().equals(Material.GLASS_BOTTLE))
            return;
        event.setCancelled(true);
        event.getPlayer().setItemInHand(new ItemStack(event.getPlayer().getItemInHand().getType(), event.getPlayer().getItemInHand().getAmount() - 1));
        event.getPlayer().getInventory().addItem(new ItemStack(Material.DRAGON_BREATH, 1));
    }
}
