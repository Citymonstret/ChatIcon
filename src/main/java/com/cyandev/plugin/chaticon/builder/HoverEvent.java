package com.cyandev.plugin.chaticon.builder;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class HoverEvent extends Event {

    public HoverEvent(final Action action, final String value) {
        super("hoverEvent", action.toString(), value);
    }

    public static enum Action {
        SHOW_TEXT, SHOW_ACHIEVEMENT, SHOW_ITEM;
        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }
}
