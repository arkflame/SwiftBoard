package dev._2lstudios.swiftboard.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import dev._2lstudios.swiftboard.swift.SwiftSidebar;

public class PlayerChangedWorldListener implements Listener {
    private final SwiftSidebar swiftSidebar;

    public PlayerChangedWorldListener(final SwiftSidebar swiftSidebar) {
        this.swiftSidebar = swiftSidebar;
    }

    @EventHandler
    public void onPlayerChangeWorld(final PlayerChangedWorldEvent event) {

    }
}
