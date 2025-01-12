package cx.rain.mc.morepotions.compat.mcmmo;

import cx.rain.mc.morepotions.api.compat.IMorePotionCompat;
import cx.rain.mc.morepotions.api.event.MorePotionBrewEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class McMMOCompat implements IMorePotionCompat {
    private McMMOCompatListener listener;

    @Override
    public boolean isFit(Plugin plugin) {
        return Bukkit.getPluginManager().getPlugin("mcMMO") != null;
    }

    @Override
    public void registerCompat(Plugin plugin) {
        listener = new McMMOCompatListener();
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void unregisterCompat(Plugin plugin) {
        MorePotionBrewEvent.getHandlerList().unregister(listener);
        listener = null;
    }
}
