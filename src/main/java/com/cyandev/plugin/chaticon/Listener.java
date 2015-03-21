package com.cyandev.plugin.chaticon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class Listener implements org.bukkit.event.Listener {

    private final ChatIcon plugin;

    public Listener(final ChatIcon plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final AsyncPlayerChatEvent event) {
        event.setFormat(event.getFormat().replace("{icon}", plugin.getIcon(event.getPlayer())));
    }

}
