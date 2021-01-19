package com.Junix03.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UniversalListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage("§f[§2+§f] §r§2§l" + player.getName() + " §r§6joined the game");
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage("§f[§4-§f] §r§6§l" + player.getName() + " §r§6left the game");
    }
}
