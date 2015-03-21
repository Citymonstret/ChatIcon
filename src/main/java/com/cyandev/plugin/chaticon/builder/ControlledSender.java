package com.cyandev.plugin.chaticon.builder;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created 2015-03-21 for ChatIcon
 *
 * @author Citymonstret
 */
public class ControlledSender {

    public static void send(final ChatMessage message, final Player player) {
        ((CraftPlayer) player).getHandle().sendMessage(IChatBaseComponent.ChatSerializer.a(message.toString()));
    }

}
