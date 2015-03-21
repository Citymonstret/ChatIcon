package com.cyandev.plugin.chaticon;

import com.cyandev.plugin.chaticon.builder.ChatMessage;
import com.cyandev.plugin.chaticon.builder.ClickEvent;
import com.cyandev.plugin.chaticon.builder.HoverEvent;
import com.cyandev.plugin.chaticon.builder.Part;
import com.cyandev.plugin.chaticon.util.StringComparison;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * Created 2015-03-19 for ChatIcon
 *
 * @author Citymonstret
 */
public class Command implements CommandExecutor {

    private ChatIcon plugin;

    public Command(final ChatIcon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("The plugin is currently not usable by the console, pardon me!");
            return true;
        }
        final boolean argsEmpty = args == null || args.length == 0;
        final Player player = (Player) commandSender;
        if (argsEmpty) {
            new ChatMessage("ChatIcon> ")
                    .color(ChatColor.DARK_RED)
                    .format(ChatColor.BOLD)
                    .with(
                            new Part("&lHelp Menu", ChatColor.GOLD)
                    )
                    .with(
                        new Part("\n /icon list - List all icons", ChatColor.GOLD)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to execute: " + ChatColor.GOLD + "/icon list"))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/icon list"))
                    )
                    .with(
                        new Part("\n /icon set [player] [icon] - Set a players icons (use 'me' to set your own)", ChatColor.GOLD)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to execute: " + ChatColor.GOLD + "/icon set [player] [icon]"))
                            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/icon set "))
                    )
                    .with(
                            new Part("\n /icon remove <player> - Remove the icon", ChatColor.GOLD)
                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to execute: " + ChatColor.GOLD + "/icon remove <player>"))
                                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/icon remove "))
                    )
                    .send(player);
        } else {
            if (!(player.hasPermission("icon." + args[0].toLowerCase()))) {
                new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                        .format(ChatColor.BOLD)
                        .with(
                                new Part("You're not permitted to use that command", ChatColor.RED)
                        ).send(player);
                    return true;
            }
            switch(args[0].toLowerCase()) {
                case "list": {
                    ChatMessage message = new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD).with(
                            new Part("Icon List (click to use)\n", ChatColor.GOLD));
                    int n = 0;
                    for (Map.Entry<String, Object> entry : plugin.icons.entrySet()) {
                        ChatColor color = n++ % 2 == 0 ? ChatColor.RED : ChatColor.GOLD;
                        message.with(new Part(" " + entry.getValue() + " " + entry.getKey(), color)
                            .event(
                                    new HoverEvent(
                                            HoverEvent.Action.SHOW_TEXT, ChatColor.RED + "Click to use that icon!"
                                    )
                            )
                            .event(
                                    new ClickEvent(
                                            ClickEvent.Action.RUN_COMMAND, "/icon set me " + entry.getKey()
                                    )
                            )
                        );
                    }
                    message.send(player);
                }
                    break;
                case "set": {
                    if (args.length < 3) {
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("Usage: /icon set [player | me] [icon]", ChatColor.RED)
                                ).send(player);
                        return true;
                    }
                    String p = args[1];
                    if (!p.equalsIgnoreCase("me") && !player.hasPermission("chaticon.set.others")) {
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("You are not permitted to change someone else's icon", ChatColor.RED)
                                ).send(player);
                        return true;
                    }
                    String i = args[2].toLowerCase();
                    if (!plugin.icons.containsKey(i)) {
                        try {
                            i = new StringComparison(i, plugin.icons.keySet().toArray()).getBestMatch().toLowerCase();
                            new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                    .format(ChatColor.BOLD)
                                    .with(
                                            new Part("That wasn't a real key, but we managed to match it anyways!", ChatColor.RED)
                                    ).send(player);
                        } catch(final Exception e) {
                            new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                    .format(ChatColor.BOLD)
                                    .with(
                                            new Part("There is no such icon...", ChatColor.RED)
                                    ).send(player);
                            break;
                        }
                    }
                    if (!player.hasPermission("chaticon.set.all") && !player.hasPermission("chaticon.set" + i)) {
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("You are not permitted to use that icon", ChatColor.RED)
                                ).send(player);
                        break;
                    }
                    UUID u = p.equalsIgnoreCase("me") ? player.getUniqueId() : getUUID(p);
                    if (u == null) {
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("That player cannot be found", ChatColor.RED)
                                ).send(player);
                        return true;
                    } else {
                        plugin.setIcon(u, i);
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("Icon set!", ChatColor.RED)
                                ).send(player);
                        return true;
                    }
                }
                case "remove": {
                    if (args.length < 2) {
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("Usage: /icon remove [player | me]", ChatColor.RED)
                                ).send(player);
                        return true;
                    }
                    String p = args[1];
                    if (!p.equalsIgnoreCase("me") && !player.hasPermission("chaticon.remove.others")) {
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("You are not permitted to change someone else's icon", ChatColor.RED)
                                ).send(player);
                        return true;
                    }
                    UUID u = p.equalsIgnoreCase("me") ? player.getUniqueId() : getUUID(p);
                    if (u == null) {
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("That player cannot be found", ChatColor.RED)
                                ).send(player);
                        return true;
                    } else {
                        plugin.removeIcon(u);
                        new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED)
                                .format(ChatColor.BOLD)
                                .with(
                                        new Part("Icon removed!", ChatColor.RED)
                                ).send(player);
                        return true;
                    }
                }
                default:
                    new ChatMessage("ChatIcon> ").color(ChatColor.DARK_RED).format(ChatColor.BOLD).with(new Part("Unknown subcommand", ChatColor.RED)).send(player);
                    break;
            }
        }
        return true;
    }

    protected UUID getUUID(final String n) {
        if (Bukkit.getOfflinePlayer(n) == null) {
            return null;
        } else {
            return Bukkit.getOfflinePlayer(n).getUniqueId();
        }
    }
}
