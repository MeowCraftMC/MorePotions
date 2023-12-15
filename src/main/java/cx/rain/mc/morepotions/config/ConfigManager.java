package cx.rain.mc.morepotions.config;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.brewing.config.EffectEntry;
import cx.rain.mc.morepotions.brewing.config.PotionEntry;
import cx.rain.mc.morepotions.brewing.config.RecipeEntry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public class ConfigManager {
    private final Plugin plugin;

    private final FileConfiguration config;

    private static final String BREWING_RECIPES_DIR_NAME = "brewing";
    private static final String CUSTOM_POTIONS_DIR_NAME = "potions";
    private static final String CUSTOM_EFFECTS_DIR_NAME = "effects";

    private static final String EXAMPLE_BREWING_RECIPES_NAME = "example_recipe.yml";
    private static final String EXAMPLE_BREWING_RECIPES_RESOURCE_PATH = "brewing/example_recipe.yml";
    private static final String EXAMPLE_CUSTOM_POTIONS_NAME = "example_potions.yml";
    private static final String EXAMPLE_CUSTOM_POTIONS_RESOURCE_PATH = "brewing/potions/example_potions.yml";
    private static final String EXAMPLE_CUSTOM_EFFECTS_NAME = "example_effects.yml";
    private static final String EXAMPLE_CUSTOM_EFFECTS_RESOURCE_PATH = "brewing/effects/example_effects.yml";

    private File brewingRecipeDir;   // Todo: qyl27: Not a local variable, maybe we have a auto-save feature?
    private File potionsDir;         // Todo: qyl27: Not a local variable, maybe we have a auto-save feature?
    private File effectsDir;         // Todo: qyl27: Not a local variable, maybe we have a auto-save feature?

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        if (allowCustomBrewingRecipe()) {
            brewingRecipeDir = new File(plugin.getDataFolder(), BREWING_RECIPES_DIR_NAME);
            potionsDir = new File(brewingRecipeDir, CUSTOM_POTIONS_DIR_NAME);
            effectsDir = new File(brewingRecipeDir, CUSTOM_EFFECTS_DIR_NAME);

            if (!brewingRecipeDir.exists()) {
                brewingRecipeDir.mkdirs();
                saveDefaultConfig(new File(brewingRecipeDir, EXAMPLE_BREWING_RECIPES_NAME), EXAMPLE_BREWING_RECIPES_RESOURCE_PATH);
            }

            if (!potionsDir.exists()) {
                potionsDir.mkdirs();
                saveDefaultConfig(new File(potionsDir, EXAMPLE_CUSTOM_POTIONS_NAME), EXAMPLE_CUSTOM_POTIONS_RESOURCE_PATH);
            }

            if (!effectsDir.exists()) {
                effectsDir.mkdirs();
                saveDefaultConfig(new File(effectsDir, EXAMPLE_CUSTOM_EFFECTS_NAME), EXAMPLE_CUSTOM_EFFECTS_RESOURCE_PATH);
            }

            loadBrewingRecipes();
        }
    }

    public boolean allowRandomEffect() {
        return config.getBoolean("features.thickRandomEffect", true);
    }

    public boolean allowCustomBrewingRecipe() {
        return config.getBoolean("features.customBrewingRecipe", true);
    }

    public boolean allowAnvilMixture() {
        return config.getBoolean("features.anvilMixPotion", true);
    }

    private void loadBrewingRecipes() {
        if (!effectsDir.isDirectory()) {
            plugin.getLogger().warning("Effects dir is not a directory! Why?");
            return;
        }

        if (!potionsDir.isDirectory()) {
            plugin.getLogger().warning("Potions dir is not a directory! Why?");
            return;
        }

        for (var file : effectsDir.listFiles()) {
            if (!file.isDirectory()) {
                var effects = load(file);
                if (effects.getBoolean("enabled", true)) {
                    // qyl27: So, Bukkit, **** you! Why not to use the generic parameter?
                    var list = effects.getList("effects", List.of());
                    for (var effect : list) {
                        if (effect instanceof EffectEntry entry) {
                            MorePotions.getInstance().getBrewingManager().addEffect(entry);
                        }
                    }
                }
            }
        }

        for (var file : potionsDir.listFiles()) {
            if (!file.isDirectory()) {
                var potions = load(file);
                if (potions.getBoolean("enabled", true)) {
                    var list = potions.getList("potions", List.of());
                    for (var potion : list) {
                        if (potion instanceof PotionEntry entry) {
                            MorePotions.getInstance().getBrewingManager().addPotion(entry);
                        }
                    }
                }
            }
        }

        for (var file : brewingRecipeDir.listFiles()) {
            if (!file.isDirectory()) {
                var recipes = load(file);
                if (recipes.getBoolean("enabled", true)) {
                    var list = recipes.getList("recipes", List.of());
                    for (var recipe : list) {
                        if (recipe instanceof RecipeEntry entry) {
                            MorePotions.getInstance().getBrewingManager().addRecipe(entry);
                        }
                    }
                }
            }
        }
    }

    /**
     * Release a config file.
     * Do nothing when file is already exists.
     * @param file Destination.
     * @param resourcePath Source path.
     */
    private void saveDefaultConfig(@Nonnull File file, @Nonnull String resourcePath) {
        if (!file.exists()) {
            plugin.saveResource(resourcePath, false);
        }
    }

    private YamlConfiguration loadOrDefault(@Nonnull File file, @Nonnull String alternativeResourcePath) {
        saveDefaultConfig(file, alternativeResourcePath);
        return YamlConfiguration.loadConfiguration(file);
    }

    private YamlConfiguration load(@Nonnull File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}
