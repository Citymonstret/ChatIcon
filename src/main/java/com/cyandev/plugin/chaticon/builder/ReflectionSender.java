package com.cyandev.plugin.chaticon.builder;

import com.cyandev.plugin.chaticon.util.ReflectionUtil;
import org.bukkit.entity.Player;

/**
 * Created 2015-03-21 for ChatIcon
 *
 * @author Citymonstret
 */
public class ReflectionSender {

    private static ReflectionUtil.RefClass craftPlayer, iChatBaseComponent, entityPlayer, chatSerializer;
    private static ReflectionUtil.RefMethod getHandle, sendMessage, a;

    public static void setup() {
        craftPlayer = ReflectionUtil.getRefClass("{cb}.entity.CraftPlayer");
        iChatBaseComponent = ReflectionUtil.getRefClass("{nms}.IChatBaseComponent");
        entityPlayer = ReflectionUtil.getRefClass("{nms}.EntityPlayer");
        chatSerializer = ReflectionUtil.getRefClass("{nms}.IChatBaseComponent$ChatSerializer");
        try {
            getHandle = craftPlayer.getMethod("getHandle");
            sendMessage = entityPlayer.getMethod("sendMessage", iChatBaseComponent);
            a = chatSerializer.getMethod("a", String.class);
        } catch(final Exception e) {
            e.printStackTrace();
        }
        // ((CraftPlayer) player).getHandle().sendMessage(IChatBaseComponent.ChatSerializer.a(message.toString()));
    }

    public static void send(final ChatMessage message, final Player player) {
        Object handle = getHandle.of(player).call();
        sendMessage.of(handle).call(a.call(message.toString()));
    }

}
