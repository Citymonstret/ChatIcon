package com.cyandev.plugin.chaticon.builder;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class Part {

    private final String s;
    private final ChatColor c;
    private final List<Event> events = new ArrayList<>();
    private final List<ChatColor> formats = new ArrayList<>();

    public Part(final String s, final ChatColor c) {
        this.s = ChatColor.translateAlternateColorCodes('&', s);
        this.c = c;
    }
    public Part(final String s) {
        this(s, ChatColor.WHITE);
    }
    public Part event(final Event event) {
        events.add(event);
        return this;
    }
    public Part format(final ChatColor color) {
        if (!color.isFormat())
            throw new RuntimeException(color.toString()
                    + " is not a format");
        if (formats.contains(color))
            formats.remove(color);
        else
            formats.add(color);
        return this;
    }
    private String eFormat() {
        final StringBuilder b = new StringBuilder();
        for (final Event e : events)
            b.append(",").append(e);
        return b.toString();
    }
    private String fFormat() {
        final StringBuilder b = new StringBuilder();
        for (final ChatColor c : formats)
            b.append(",\"").append(getColor(c)).append("\":\"true\"");
        for (final ChatColor c : new ChatColor[] { ChatColor.BOLD, ChatColor.ITALIC, ChatColor.UNDERLINE, ChatColor.STRIKETHROUGH}) {
            if (formats.contains(c))
                continue;
            b.append(",\"").append(getColor(c)).append("\":\"false\"");
        }
        return b.toString();
    }
    @Override
    public String toString() {
        return String
                .format("{\"text\":\"%s\",\"color\":\"%s\"%s%s}", s,
                        getColor(c), events.isEmpty() ? "" : eFormat(),
                        fFormat());
    }

    private String getColor(final ChatColor color) {
        return color.name().toLowerCase();
    }
}
