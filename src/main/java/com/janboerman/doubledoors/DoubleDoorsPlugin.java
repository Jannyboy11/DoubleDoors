package com.janboerman.doubledoors;

import com.janboerman.doubledoors.listener.BlockRedstoneListener;
import com.janboerman.doubledoors.listener.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

/// The DoubleDoors plugin main class.
/// See [com.janboerman.doubledoors.api.DoubleDoorsAPI] for the public API.
public class DoubleDoorsPlugin extends JavaPlugin {

    private final Metrics metrics = new Metrics(this);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BlockRedstoneListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);

        metrics.ready();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }

    Path getJarFilePath() {
        return getFile().toPath();
    }
}
