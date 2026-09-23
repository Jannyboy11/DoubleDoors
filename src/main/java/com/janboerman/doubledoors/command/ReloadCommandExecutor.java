package com.janboerman.doubledoors.command;

import com.janboerman.doubledoors.DoubleDoorsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/// Command which reloads the DoubleDoors plugin config from the config.yml file on disk.
public class ReloadCommandExecutor implements CommandExecutor {

    private final DoubleDoorsPlugin plugin;

    public ReloadCommandExecutor(DoubleDoorsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "DoubleDoors config reloaded!");
    }
}
