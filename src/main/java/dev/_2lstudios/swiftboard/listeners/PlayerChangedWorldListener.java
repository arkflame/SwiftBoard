package dev._2lstudios.swiftboard.listeners;

import java.util.List;

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
        final List<String> newLines = swiftSidebarConfig.getLines(worldName);

        if (newLines != null) {
            swiftSidebar.setLines(player, newLines);
        } else if (!swiftSidebar.getWorld().equals("default")) {
            swiftSidebar.setLines(player, null);
        }
    }
}
