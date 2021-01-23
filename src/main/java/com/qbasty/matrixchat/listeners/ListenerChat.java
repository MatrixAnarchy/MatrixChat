package com.qbasty.matrixchat.listeners;

import com.qbasty.matrixchat.MatrixChat;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Listener;

public class ListenerChat implements Listener
{
    MatrixChat plugin;
    List<String> ignoreList;
    boolean chatDisabled;

    public ListenerChat(final MatrixChat plugin) {
        this.ignoreList = new ArrayList<String>();
        this.chatDisabled = false;
        this.plugin = plugin;
    }

    @EventHandler
    public void playerChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        e.setMessage(e.getMessage().replace(">", "§a>"));
        if (p.hasPermission("matrixchat.red")) {
            e.setMessage(e.getMessage().replace("<", "§c<"));
        }
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final String uuid = player.getUniqueId().toString();
            if (this.plugin.ignoreCfg.getBoolean(uuid + ".chatDisabled")) {
                e.getRecipients().remove(player);]
            }
            else {
                if (this.plugin.ignoreCfg.getStringList(uuid + ".ignoring") == null) {
                    continue;
                }
                this.ignoreList = (List<String>)this.plugin.ignoreCfg.getStringList(uuid + ".ignoring");
                if (!this.ignoreList.contains(p.getName())) {
                    continue;
                }
                e.getRecipients().remove(player);
            }
        }
    }
}
