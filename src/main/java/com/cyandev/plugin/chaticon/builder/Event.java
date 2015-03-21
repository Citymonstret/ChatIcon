package com.cyandev.plugin.chaticon.builder;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class Event {

    private final String key, value, action;

    public Event(final String key, final String action, final String value) {
        this.key = key;
        this.action = action;
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"%s\":{\"action\":\"%s\",\"value\":\"%s\"}"
                .replaceFirst("%s", key)
                .replaceFirst("%s", action)
                .replaceFirst("%s", value);
    }
}
