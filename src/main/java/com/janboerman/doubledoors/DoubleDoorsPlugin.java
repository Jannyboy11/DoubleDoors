package com.janboerman.doubledoors;

import com.janboerman.doubledoors.command.ReloadCommandExecutor;
import com.janboerman.doubledoors.listener.BlockRedstoneListener;
import com.janboerman.doubledoors.listener.PlayerInteractListener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Objects;

/// The DoubleDoors plugin main class.
/// See [com.janboerman.doubledoors.api.DoubleDoorsAPI] for the public API.
public class DoubleDoorsPlugin extends JavaPlugin {

    private final Metrics metrics = new Metrics(this);

    private Permission interactPermission;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        //noinspection ConstantConditions - Suppress nullability warning in IntelliJ.
        getCommand("doubledoors-reload").setExecutor(new ReloadCommandExecutor(this));

        getServer().getPluginManager().registerEvents(new BlockRedstoneListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        interactPermission = Objects.requireNonNull(getServer().getPluginManager().getPermission("doubledoors.interact"));

        metrics.ready();
    }

    @Override
    public void onDisable() {
        interactPermission = null;

        metrics.shutdown();
    }

    /// Get the permission required for automatically interacting with adjacent doors/trapdoors/fence gates.
    /// @return the permission, or null if DoubleDoors is not enabled
    public Permission getInteractPermission() {
        return interactPermission;
    }

    /// Get whether redstone interactions are enabled for adjacent doors/trapdoors/fence gates.
    /// @return true if redstone interactions are enabled, otherwise false
    public boolean isRedstoneEnabled() {
        return getConfig().getBoolean("redstone");
    }

    Path getJarFilePath() {
        return getFile().toPath();
    }
}
