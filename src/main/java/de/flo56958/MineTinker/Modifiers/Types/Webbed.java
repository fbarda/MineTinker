package de.flo56958.MineTinker.Modifiers.Types;

import de.flo56958.MineTinker.Data.ToolType;
import de.flo56958.MineTinker.Events.MTEntityDamageByEntityEvent;
import de.flo56958.MineTinker.Main;
import de.flo56958.MineTinker.Modifiers.Craftable;
import de.flo56958.MineTinker.Modifiers.Modifier;
import de.flo56958.MineTinker.Utilities.ChatWriter;
import de.flo56958.MineTinker.Utilities.ConfigurationManager;
import de.flo56958.MineTinker.Utilities.ItemGenerator;
import de.flo56958.MineTinker.Utilities.Modifiers_Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;

public class Webbed extends Modifier implements Craftable {

    private int duration;
    private double durationMultiplier;
    private int effectAmplifier;

    public Webbed() {
        super(ModifierType.WEBBED,
                new ArrayList<>(Arrays.asList(ToolType.AXE, ToolType.BOW, ToolType.SWORD,
                                                ToolType.HELMET, ToolType.CHESTPLATE, ToolType.LEGGINGS, ToolType.BOOTS, ToolType.ELYTRA)),
                Main.getPlugin());
    }

    @Override
    public void reload() {
        FileConfiguration config = getConfig();
    	config.options().copyDefaults(true);
    	
    	String key = "Webbed";
    	config.addDefault(key + ".allowed", true);
    	config.addDefault(key + ".name", key);
    	config.addDefault(key + ".name_modifier", "Compressed Cobweb");
    	config.addDefault(key + ".description", "Slowes down enemies!");
        config.addDefault(key + ".description_modifier", "%WHITE%Modifier-Item for the Webbed-Modifier");
        config.addDefault(key + ".Color", "%WHITE%");
        config.addDefault(key + ".MaxLevel", 3);
    	config.addDefault(key + ".Duration", 60); //ticks (20 ticks ~ 1 sec)
    	config.addDefault(key + ".Sound", true);
    	config.addDefault(key + ".DurationMultiplier", 1.2);//Duration * (Multiplier^Level)
    	config.addDefault(key + ".EffectAmplifier", 2); //per Level (Level 1 = 0, Level 2 = 2, Level 3 = 4, ...)
    	config.addDefault(key + ".Recipe.Enabled", true);
    	config.addDefault(key + ".Recipe.Top", "WWW");
    	config.addDefault(key + ".Recipe.Middle", "WWW");
    	config.addDefault(key + ".Recipe.Bottom", "WWW");
    	config.addDefault(key + ".Recipe.Materials.W", "COBWEB");
    	
    	ConfigurationManager.saveConfig(config);
        
        init(getConfig().getString(key + ".name"),
                "[" + getConfig().getString(key + ".name_modifier") + "] " + getConfig().getString(key + ".description"),
                ChatWriter.getColor(config.getString(key + ".Color")),
                getConfig().getInt(key + ".MaxLevel"),
                modManager.createModifierItem(Material.COBWEB, ChatWriter.getColor(config.getString(key + ".Color")) + config.getString(key + ".name_modifier"), ChatWriter.addColors(config.getString(key + ".description_modifier")), this));
        
        this.duration = config.getInt(key + ".Duration");
        this.durationMultiplier = config.getDouble(key + ".DurationMultiplier");
        this.effectAmplifier = config.getInt(key + ".EffectAmplifier");
    }

    @Override
    public ItemStack applyMod(Player p, ItemStack tool, boolean isCommand) {
        return Modifier.checkAndAdd(p, tool, this, "webbed", isCommand);
    }

    @Override
    public void removeMod(ItemStack tool) { }

    @EventHandler
    public void effect(MTEntityDamageByEntityEvent event) {
        if (event.isCancelled() || !this.isAllowed()) { return; }
        if (!(event.getEvent().getEntity() instanceof LivingEntity)) { return; }

        Player p = event.getPlayer();
        ItemStack tool = event.getTool();
        LivingEntity e = (LivingEntity) event.getEvent().getEntity();

        if (!p.hasPermission("minetinker.modifiers.webbed.use")) { return; }
        if (e.isDead()) { return; }
        if (!modManager.hasMod(tool, this)) { return; }

        int level = modManager.getModLevel(tool, this);

        int duration = (int) (this.duration * Math.pow(this.durationMultiplier, (level - 1)));
        int amplifier = this.effectAmplifier * (level - 1) / 2;

        e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier, false, false));
        ChatWriter.log(false, p.getDisplayName() + " triggered Webbed on " + ItemGenerator.getDisplayName(tool) + ChatColor.GRAY + " (" + tool.getType().toString() + ")!");
    }

    @Override
    public void registerCraftingRecipe() {
        _registerCraftingRecipe(getConfig(), this, "Webbed", "Modifier_Webbed");
    }
    
    private static FileConfiguration getConfig() {
    	return ConfigurationManager.getConfig(Modifiers_Config.Webbed);
    }

    @Override
    public boolean isAllowed() {
    	return getConfig().getBoolean("Webbed.allowed");
    }
}
