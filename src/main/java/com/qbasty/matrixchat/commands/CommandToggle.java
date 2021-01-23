package com.qbasty.matrixchat.commands;

import com.qbasty.matrixchat.MatrixChat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CommandToggle implements CommandExecutor
{
    MatrixChat plugin;

    public CommandToggle(final MatrixChat plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to toggle chat or private messaging.");
            return true;
        }
        final Player p = (Player)sender;
        if (!p.hasPermission("matrixchat.toggle")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to toggle chat or private messaging.");
            return true;
        }
        final String uuid = p.getUniqueId().toString();
        if (cmd.getName().equalsIgnoreCase("togglechat")) {
            final boolean chatDisabled = this.plugin.ignoreCfg.getBoolean(uuid + ".chatDisabled");
            if (chatDisabled) {
                this.plugin.ignoreCfg.set(uuid + ".chatDisabled", (Object)false);
                p.sendMessage(ChatColor.GREEN + "You have enabled global chat.");
                this.plugin.saveConfig();
                return true;
            }
            this.plugin.ignoreCfg.set(uuid + ".chatDisabled", (Object)true);
            p.sendMessage(ChatColor.RED + "You have disabled global chat.");
            this.plugin.saveConfig();
        }
        if (cmd.getName().equalsIgnoreCase("togglepms")) {
            final boolean pmsDisabled = this.plugin.ignoreCfg.getBoolean(uuid + ".pmsDisabled");
            if (pmsDisabled) {
                this.plugin.ignoreCfg.set(uuid + ".pmsDisabled", (Object)false);
                p.sendMessage(ChatColor.GREEN + "You have enabled private messaging.");
                this.plugin.saveConfig();
                return true;
            }
            this.plugin.ignoreCfg.set(uuid + ".pmsDisabled", (Object)true);
            p.sendMessage(ChatColor.RED + "You have disabled private messaging.");
            this.plugin.saveConfig();
        }
        return false;
    }
}