package de.flo56958.MineTinker;

import de.flo56958.MineTinker.Commands.Commands;
import de.flo56958.MineTinker.Data.CraftingRecipes;
import de.flo56958.MineTinker.Data.PlayerData;
import de.flo56958.MineTinker.Data.Strings;
import de.flo56958.MineTinker.Listeners.*;
import de.flo56958.MineTinker.Utilities.ChatWriter;
import de.flo56958.MineTinker.bStats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("minetinker").setExecutor(new Commands());
        ChatWriter.log(false, "Registered commands!");

        loadConfig();

        if (getConfig().getBoolean("AllowCrafting")) {
            Bukkit.getPluginManager().registerEvents(new CraftingGrid9Listener(), this);
        }
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        if (!getConfig().getBoolean("AllowEnchanting")) {
            Bukkit.getPluginManager().registerEvents(new EnchantingTableListener(), this);
        }
        if (getConfig().getBoolean("Elevator.enabled")) {
            Bukkit.getPluginManager().registerEvents(new ElevatorListener(), this);
            CraftingRecipes.registerElevatorMotor();
        }
        if (getConfig().getBoolean("Builderswands.enabled")) {
            Bukkit.getPluginManager().registerEvents(new BuildersWandListener(),this);
            CraftingRecipes.registerBuildersWands();
        }

        ChatWriter.log(false, "Registered events!");

        if (getConfig().getBoolean("Modifiers.Auto-Smelt.allowed")) {
            CraftingRecipes.registerAutoSmeltModifier();
        }
        if (getConfig().getBoolean("Modifiers.Directing.allowed")) {
            CraftingRecipes.registerDirectingModifier();
        }
        if (getConfig().getBoolean("Modifiers.Ender.allowed")) {
            CraftingRecipes.registerEnderModifier();
        }
        if (getConfig().getBoolean("Modifiers.Glowing.allowed")) {
            CraftingRecipes.registerGlowingModifier();
        }
        if (getConfig().getBoolean("Modifiers.Haste.allowed")) {
            CraftingRecipes.registerHasteModifier();
        }
        if (getConfig().getBoolean("Modifiers.Luck.allowed")) {
            CraftingRecipes.registerLuckModifier();
        }
        if (getConfig().getBoolean("Modifiers.Reinforced.allowed")) {
            CraftingRecipes.registerReinforcedModifier();
        }
        if (getConfig().getBoolean("Modifiers.Sharpness.allowed")) {
            CraftingRecipes.registerSharpnessModifier();
        }
        if (getConfig().getBoolean("Modifiers.Shulking.allowed")) {
            CraftingRecipes.registerShulkingModifier();
        }
        if (getConfig().getBoolean("Modifiers.Timber.allowed")) {
            CraftingRecipes.registerTimberModifier();
        }
        if (getConfig().getBoolean("Modifiers.Webbed.allowed")) {
            CraftingRecipes.registerWebbedModifier();
        }
        ChatWriter.log(false, "Registered crafting recipes!");

        if (getConfig().getBoolean("logging.metrics")) {
            Metrics metrics = new Metrics(this);
            Bukkit.getConsoleSender().sendMessage(Strings.CHAT_PREFIX + " Started Metrics-service! Thank you for enabling it in the config! It helps to maintain the Plugin and fix bugs. (Data is anonymously sent)");
        }
        ChatWriter.log(false, "Standard logging is enabled! You can disable it in the config!");
        ChatWriter.log(true, "Debug logging is enabled! You should disable it in the config!");

        for (Player current : Bukkit.getServer().getOnlinePlayers()) {
            PlayerData.HASPOWER.put(current, false);
            PlayerData.BLOCKFACE.put(current, null);
        }
    }

    public void onDisable() {
        ChatWriter.log(false, "Shutting down!");
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        ChatWriter.log(false, "Config loaded!");
    }

    public static Plugin getPlugin() { //necessary to do getConfig() in other classes
        return Bukkit.getPluginManager().getPlugin("MineTinker");
    }
}
