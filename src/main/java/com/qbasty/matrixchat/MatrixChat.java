package com.qbasty.matrixchat;

import java.io.IOException;
import com.qbasty.matrixchat.commands.CommandIgnore;
import com.qbasty.matrixchat.commands.CommandMessage;
import com.qbasty.matrixchat.commands.CommandToggle;
import com.qbasty.matrixchat.listeners.ListenerChat;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.HashMap;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class MatrixChat extends JavaPlugin
{
    public File ignoreFile;
    public FileConfiguration ignoreCfg;
    public HashMap<String, String> lastMessage;

    public MatrixChat() {
        this.ignoreFile = new File("plugins/MatrixChat", "ignoreList.yml");
        this.ignoreCfg = (FileConfiguration)YamlConfiguration.loadConfiguration(this.ignoreFile);
        this.lastMessage = new HashMap<String, String>();
    }

    public void onEnable() {
        this.getCommand("message").setExecutor((CommandExecutor)new CommandMessage(this));
        this.getCommand("reply").setExecutor((CommandExecutor)new CommandMessage(this));
        this.getCommand("ignore").setExecutor((CommandExecutor)new CommandIgnore(this));
        this.getCommand("unignore").setExecutor((CommandExecutor)new CommandIgnore(this));
        this.getCommand("ignorelist").setExecutor((CommandExecutor)new CommandIgnore(this));
        this.getCommand("toggleChat").setExecutor((CommandExecutor)new CommandToggle(this));
        this.getCommand("togglepms").setExecutor((CommandExecutor)new CommandToggle(this));
        this.getServer().getPluginManager().registerEvents((Listener)new ListenerChat(this), (Plugin)this);
        this.getConfig().addDefault("senderFormat", (Object)"&7[you -> %player%]");
        this.getConfig().addDefault("recieverFormat", (Object)"&7[%player% -> you]");
        this.saveDefaultConfig();
    }

    public void saveConfig() {
        try {
            this.ignoreCfg.save(this.ignoreFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.ignoreCfg = (FileConfiguration)YamlConfiguration.loadConfiguration(this.ignoreFile);
    }
}