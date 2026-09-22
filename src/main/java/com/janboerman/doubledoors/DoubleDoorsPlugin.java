package com.janboerman.doubledoors;

import org.bukkit.plugin.java.JavaPlugin;

public class DoubleDoorsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

}
