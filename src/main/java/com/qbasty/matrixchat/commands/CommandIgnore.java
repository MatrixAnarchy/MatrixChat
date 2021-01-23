package com.qbasty.matrixchat.commands;

import java.util.List;

import com.qbasty.matrixchat.MatrixChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandIgnore implements CommandExecutor
{
    MatrixChat plugin;

    public CommandIgnore(final MatrixChat plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {
        List<String> ignoring = new ArrayList<String>();
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to ignore others.");
            return true;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("matrixchat.ignore")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to use this command..");
            return true;
        }
        final String uuid = p.getUniqueId().toString();
        if (cmd.getName().equalsIgnoreCase("ignore")) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Usage: /ignore <player>");
                return true;
            }
            final Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            if (this.plugin.ignoreCfg.get(uuid + ".ignoring") == null) {
                ignoring.add(target.getName());
                this.plugin.ignoreCfg.set(uuid + ".ignoring", (Object)ignoring);
                p.sendMessage(ChatColor.GREEN + "You are now ignoring " + target.getName() + ".");
                this.plugin.saveConfig();
                ignoring.clear();
                return true;
            }
            if (this.plugin.ignoreCfg.getStringList(uuid + ".ignoring").contains(target.getName())) {
                p.sendMessage(ChatColor.RED + "You are already ignoring " + target.getName() + ".");
                return true;
            }
            ignoring = (List<String>)this.plugin.ignoreCfg.getStringList(uuid + ".ignoring");
            ignoring.add(target.getName());
            this.plugin.ignoreCfg.set(uuid + ".ignoring", (Object)ignoring);
            p.sendMessage(ChatColor.GREEN + "You are now ignoring " + target.getName() + ".");
            this.plugin.saveConfig();
            ignoring.clear();
        }
        if (cmd.getName().equalsIgnoreCase("unignore")) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Usage: /unignore <player>");
                return true;
            }
            final Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
            if (this.plugin.ignoreCfg.get(uuid + ".ignoring") == null || !this.plugin.ignoreCfg.getStringList(uuid + ".ignoring").contains(target.getName())) {
                p.sendMessage(ChatColor.RED + "You are not ignoring " + target.getName() + ".");
                return true;
            }
            ignoring = (List<String>)this.plugin.ignoreCfg.getStringList(uuid + ".ignoring");
            ignoring.remove(target.getName());
            this.plugin.ignoreCfg.set(uuid + ".ignoring", (Object)ignoring);
            p.sendMessage(ChatColor.GREEN + "You are no longer ignoring " + target.getName() + ".");
            this.plugin.saveConfig();
            ignoring.clear();
        }
        if (cmd.getName().equalsIgnoreCase("ignorelist")) {
            if (this.plugin.ignoreCfg.get(uuid + ".ignoring") == null || this.plugin.ignoreCfg.getStringList(uuid + ".ignoring").isEmpty()) {
                p.sendMessage(ChatColor.RED + "You are not ignoring anyone.");
                return true;
            }
            p.sendMessage(ChatColor.GREEN + "You are ignoring: ");
            for (final String playerName : this.plugin.ignoreCfg.getStringList(uuid + ".ignoring")) {
                p.sendMessage(ChatColor.GRAY + " - " + ChatColor.GOLD + playerName);
            }
        }
        return false;
    }
}
