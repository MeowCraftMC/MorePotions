package cx.rain.mc.morepotions.compat.mcmmo;

import com.gmail.nossr50.datatypes.skills.alchemy.PotionStage;
import com.gmail.nossr50.util.ContainerMetadataUtils;
import com.gmail.nossr50.util.player.UserManager;
import cx.rain.mc.morepotions.api.event.MorePotionBrewEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class McMMOCompatListener implements Listener {
    @EventHandler
    public void onBrewEnd(MorePotionBrewEvent event) {
        var block = event.getBrewingStand();
        var player = ContainerMetadataUtils.getContainerOwner(block);
        if (player == null) {
            return;
        }

        var mcmmoPlayer = UserManager.getOfflinePlayer(player);
        if (mcmmoPlayer == null) {
            return;
        }

        mcmmoPlayer.getAlchemyManager()
                .handlePotionBrewSuccesses(PotionStage.ONE, 1);
    }
}
