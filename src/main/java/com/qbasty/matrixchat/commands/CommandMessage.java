package com.qbasty.matrixchat.commands;

import com.qbasty.matrixchat.MatrixChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandMessage implements CommandExecutor
{
    MatrixChat plugin;

    public CommandMessage(final MatrixChat plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to message others.");
            return true;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("matrixchat.message")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to message others.");
            return true;
        }
        if (this.plugin.ignoreCfg.getBoolean(p.getUniqueId().toString() + ".pmsDisabled")) {
            p.sendMessage(ChatColor.RED + "You have private messaging disabled.");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("message")) {
            if (args.length <= 1) {
                p.sendMessage(ChatColor.RED + "Usage: /message <player> <contents>");
                return true;
            }
            final Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            if (target == p) {}
            if (this.plugin.ignoreCfg.getBoolean(target.getUniqueId().toString() + ".pmsDisabled")) {
                p.sendMessage(ChatColor.RED + "That player has private messaging disabled.");
                return true;
            }
            if (this.plugin.ignoreCfg.getStringList(p.getUniqueId().toString() + ".ignoring") != null && this.plugin.ignoreCfg.getStringList(p.getUniqueId().toString() + ".ignoring").contains(p.getName())) {
                p.sendMessage(ChatColor.RED + "You cannot message that player.");
                return true;
            }
            if (this.plugin.ignoreCfg.getStringList(target.getUniqueId().toString() + ".ignoring") != null && this.plugin.ignoreCfg.getStringList(target.getUniqueId().toString() + ".ignoring").contains(p.getName())) {
                p.sendMessage(ChatColor.RED + "You cannot message that player.");
                return true;
            }
            this.sendMessage(p, target, args, 1);
        }
        if (cmd.getName().equalsIgnoreCase("reply")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Usage: /reply <contents>");
                return true;
            }
            if (!this.plugin.lastMessage.containsKey(p.getName())) {
                p.sendMessage(ChatColor.RED + "You have not previously sent or a recieved a message.");
                return true;
            }
            final Player target = this.plugin.getServer().getPlayer((String)this.plugin.lastMessage.get(p.getName()));
            if (target == null) {
                p.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            if (target == p) {}
            if (this.plugin.ignoreCfg.getBoolean(target.getUniqueId().toString() + ".pmsDisabled")) {
                p.sendMessage(ChatColor.RED + "That player has private messaging disabled.");
                return true;
            }
            if (this.plugin.ignoreCfg.getStringList(p.getUniqueId().toString() + ".ignoring") != null && this.plugin.ignoreCfg.getStringList(p.getUniqueId().toString() + ".ignoring").contains(p.getName())) {
                p.sendMessage(ChatColor.RED + "You cannot message that player.");
                return true;
            }
            if (this.plugin.ignoreCfg.getStringList(target.getUniqueId().toString() + ".ignoring") != null && this.plugin.ignoreCfg.getStringList(target.getUniqueId().toString() + ".ignoring").contains(p.getName())) {
                p.sendMessage(ChatColor.RED + "You cannot message that player.");
                return true;
            }
            this.sendMessage(p, target, args, 0);
        }
        return false;
    }

    public void sendMessage(final Player sender, final Player target, final String[] args, final int startIndex) {
        final StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        final String msg = sb.toString();
        if (this.plugin.getConfig().getString("senderFormat") == null) {
            sender.sendMessage(ChatColor.GRAY + "[you -> " + sender.getName() + "] " + msg);
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("senderFormat").replace("%player%", target.getName())) + " " + msg);
        }
        if (this.plugin.getConfig().getString("recieverFormat") == null) {
            target.sendMessage(ChatColor.GRAY + "[" + sender.getName() + " -> you] " + msg);
        }
        else {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("recieverFormat").replace("%player%", sender.getName())) + " " + msg);
        }
        this.plugin.lastMessage.put(sender.getName(), target.getName());
        this.plugin.lastMessage.put(target.getName(), sender.getName());
    }
}
