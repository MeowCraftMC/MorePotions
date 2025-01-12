package cx.rain.mc.morepotions;

import cx.rain.mc.morepotions.api.MorePotionsAPI;
import cx.rain.mc.morepotions.api.data.RecipeEntry;
import cx.rain.mc.morepotions.api.data.PotionEntry;
import cx.rain.mc.morepotions.command.MorePotionsCommand;
import cx.rain.mc.morepotions.compat.CompatManager;
import cx.rain.mc.morepotions.config.ConfigManager;
import cx.rain.mc.morepotions.brewing.BrewingManager;
import cx.rain.mc.morepotions.listener.BrewingListener;
import cx.rain.mc.morepotions.listener.ClickDragonEggListener;
import cx.rain.mc.morepotions.listener.DrinkPotionListener;
import cx.rain.mc.morepotions.api.data.EffectEntry;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class MorePotions extends JavaPlugin {
    private static MorePotions INSTANCE;

    static {
        ConfigurationSerialization.registerClass(PotionEntry.class);
        ConfigurationSerialization.registerClass(EffectEntry.class);
        ConfigurationSerialization.registerClass(RecipeEntry.class);
    }

    private final BrewingManager brewingManager;

    private final ConfigManager configManager;

    private final CompatManager compatManager;

    public MorePotions() {
        INSTANCE = this;
        MorePotionsAPI.setInstance(this);

        brewingManager = new BrewingManager();
        configManager = new ConfigManager(this);
        compatManager = new CompatManager();
    }

    public BrewingManager getBrewingManager() {
        return brewingManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public CompatManager getCompatManager() {
        return compatManager;
    }

    public static MorePotions getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BrewingListener(), this);
        getServer().getPluginManager().registerEvents(new DrinkPotionListener(), this);
        getServer().getPluginManager().registerEvents(new ClickDragonEggListener(), this);

        var pluginCommand = getCommand("morepotions");
        if (pluginCommand != null) {
            var command = new MorePotionsCommand();
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
        }

        if (getConfigManager().allowBrewingCompat()) {
            getCompatManager().load();
            getCompatManager().register();
        }
    }

    public void onReload() {
        getCompatManager().unregister();

        getConfigManager().reload();

        if (getConfigManager().allowBrewingCompat()) {
            getCompatManager().load();
            getCompatManager().register();
        }
    }

    @Override
    public void onDisable() {
        getCompatManager().unregister();
    }
}
