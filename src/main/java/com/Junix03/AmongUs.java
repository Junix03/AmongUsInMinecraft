package com.Junix03;

import com.Junix03.utils.UniversalListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class AmongUs extends JavaPlugin {

    public static AmongUs INSTANCE;

    @Override
    public void onEnable() {
        // Plugin startup logic
        init();
    }

    private void init() {
        INSTANCE = this;

        getServer().getPluginManager().registerEvents(new UniversalListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
