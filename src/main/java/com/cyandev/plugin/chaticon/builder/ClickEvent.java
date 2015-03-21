package com.cyandev.plugin.chaticon.builder;

/**
 * Created 2015-03-19 for PluginInfo
 *
 * @author Citymonstret
 */
public class ClickEvent extends Event {

    public ClickEvent(final Action action, final String value) {
        super("clickEvent", action.toString(), value);
    }

    public static enum Action {
        OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND;
        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }
}
