package com.cyandev.plugin.chaticon;

import com.cyandev.plugin.chaticon.builder.ReflectionSender;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class ChatIcon extends JavaPlugin {

    private final String SUPPORTED_VERSION = "v1_8_R2";
    protected final Map<String, Object> icons = new HashMap<>();
    private final Map<String, Object> storage = new HashMap<>();

    public static boolean NMS_CONTROL = false;

     {
        icons.put("plane", "\u2708");
        icons.put("snowman", "\u2603");
        icons.put("coffee", "\u2615");
    }

    @Override
    public void onEnable() {
        if (checkVersion()) {
            getLogger().log(Level.WARNING, "Unsupported version - The plugin is built for: " + SUPPORTED_VERSION);
            getLogger().log(Level.WARNING, "Will attempt to use the NMS Control System");
            // getPluginLoader().disablePlugin(this);
        } else {
            setupConfig();
            getCommand("icon").setExecutor(new Command(this));
            Bukkit.getPluginManager().registerEvents(new Listener(this), this);

            if (NMS_CONTROL) {
                ReflectionSender.setup();
                getLogger().log(Level.WARNING, "Using ReflectionSender -> Might cause issues!");
            }
        }
    }

    @Override
    public void onDisable() {
        this.endConfig();
    }

    private void setupConfig() {
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        config.options().copyHeader(true);
        config.options().header("ChatIcon> Configuration - Find more at http://unicodefor.us/");
        config.addDefault("icons", icons);
        config.addDefault("storage", storage);
        config.addDefault("nmsControl", false);
        saveConfig();
        reloadConfig();

        try {
            icons.putAll(config.getConfigurationSection("icons").getValues(false));
        } catch(final Exception ignore) {
            // Ignore
        }
        try {
            storage.putAll(config.getConfigurationSection("storage").getValues(false));
        } catch(final Exception ignore) {
            // Ignore
        }
        NMS_CONTROL = config.getBoolean("nmsControl");
    }

    private void endConfig() {
        FileConfiguration config = getConfig();
        config.set("storage", storage);
        saveConfig();
    }

    protected boolean checkVersion() {
        try {
            Class.forName("net.minecraft.server.%s.IChatBaseComponent".replaceFirst("%s", SUPPORTED_VERSION), false, null);
        } catch(final Exception e) {
            return false;
        }
        return true;
    }

    public String getIcon(final Player player) {
        if (storage.containsKey(player.getUniqueId().toString())) {
            return icons.get(storage.get(player.getUniqueId().toString())).toString();
        }
        return "";
    }

    public void setIcon(final UUID uuid, final String string) {
        storage.put(uuid.toString(), string);
    }

    public void removeIcon(final UUID uuid) {
        if (storage.containsKey(uuid.toString()))
            storage.remove(uuid.toString());
    }
}
