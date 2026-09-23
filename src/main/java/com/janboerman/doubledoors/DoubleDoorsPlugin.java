package com.janboerman.doubledoors;

import com.janboerman.doubledoors.listener.BlockRedstoneListener;
import com.janboerman.doubledoors.listener.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

/// The DoubleDoors plugin main class.
/// See [com.janboerman.doubledoors.api.DoubleDoorsAPI] for the public API.
public class DoubleDoorsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BlockRedstoneListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
    }

}
