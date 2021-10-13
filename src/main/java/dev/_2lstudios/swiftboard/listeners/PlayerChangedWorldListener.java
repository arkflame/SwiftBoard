package dev._2lstudios.swiftboard.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import dev._2lstudios.swiftboard.swift.SwiftSidebar;
import dev._2lstudios.swiftboard.swift.config.SwiftSidebarConfig;

public class PlayerChangedWorldListener implements Listener {
    private final SwiftSidebar swiftSidebar;
    private final SwiftSidebarConfig swiftSidebarConfig;

    public PlayerChangedWorldListener(final SwiftSidebar swiftSidebar, final SwiftSidebarConfig swiftSidebarConfig) {
        this.swiftSidebar = swiftSidebar;
        this.swiftSidebarConfig = swiftSidebarConfig;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangeWorld(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final String worldName = player.getWorld().getName();

        swiftSidebar.setLines(player, swiftSidebarConfig.getLines(worldName));
    }
}
