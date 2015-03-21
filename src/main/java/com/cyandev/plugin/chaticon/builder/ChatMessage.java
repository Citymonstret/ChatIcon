package com.cyandev.plugin.chaticon.builder;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Chat Message
 *
 * @author Citymonstret
 */
public class ChatMessage {
    public List<Event> events = new ArrayList<>();
    public List<Part> with = new ArrayList<>();
    private final String text;
    private ChatColor color = ChatColor.WHITE;
    private final List<ChatColor> formats = new ArrayList<>();
    public ChatMessage(final String text) {
        this.text = text;
    }
    public ChatMessage color(final ChatColor color) {
        this.color = color;
        return this;
    }
    public ChatMessage with(final Part part) {
        with.add(part);
        return this;
    }
    public ChatMessage event(final Event event) {
        events.add(event);
        return this;
    }
    public ChatMessage format(final ChatColor color) {
        if (!color.isFormat())
            throw new RuntimeException(color.toString()
                    + " is not a color");
        if (formats.contains(color))
            formats.remove(color);
        else
            formats.add(color);
        return this;
    }
    @Override
    public String toString() {
        String format = "{\"text\":\"%s\",\"color\":\"%s\"%s%s%s}";
        format = format.replaceFirst("%s", text);
        format = format.replaceFirst("%s", getColor(color));
        StringBuilder w = new StringBuilder("");
        if (with != null && !with.isEmpty()) {
            final Iterator<Part> iterator = with.iterator();
            w.append(",\"extra\":[");
            while (iterator.hasNext()) {
                w.append(iterator.next());
                if (iterator.hasNext())
                    w.append(",");
            }
            w.append("]");
        }
        format = format.replaceFirst("%s", w.toString());
        w = new StringBuilder();
        if (events.isEmpty())
            format = format.replaceFirst("%s", "");
        else {
            for (final Event event : events)
                w.append(",").append(event);
            format = format.replaceFirst("%s", w.toString());
        }
        w = new StringBuilder();
        if (formats.isEmpty())
            format = format.replaceFirst("%s", "");
        else {
            for (final ChatColor f : formats)
                w.append(",\"").append(getColor(f)).append("\":\"true\"");
            format = format.replaceFirst("%s", w.toString());
        }
        return format;
    }

    public void send(final Player player) {
        ((CraftPlayer) player).getHandle().sendMessage(IChatBaseComponent.ChatSerializer.a(toString()));
    }

    public void broadcast() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            send(p);
        }
    }

    private String getColor(final ChatColor color) {
        return color.name().toLowerCase();
    }
}