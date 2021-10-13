package dev._2lstudios.swiftboard.listeners;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import dev._2lstudios.swiftboard.swift.SwiftNametag;
import dev._2lstudios.swiftboard.swift.SwiftSidebar;
import dev._2lstudios.swiftboard.swift.config.SwiftSidebarConfig;

public class PlayerJoinListener implements Listener {
    private final ScoreboardManager scoreboardManager;
    private final SwiftSidebar swiftSidebar;
    private final SwiftNametag swiftNametag;
    private final SwiftSidebarConfig swiftSidebarConfig;

    public PlayerJoinListener(final ScoreboardManager scoreboardManager, final SwiftSidebar swiftSidebar,
            final SwiftNametag swiftNametag, final SwiftSidebarConfig swiftSidebarConfig) {
        this.swiftSidebar = swiftSidebar;
        this.scoreboardManager = scoreboardManager;
        this.swiftNametag = swiftNametag;
        this.swiftSidebarConfig = swiftSidebarConfig;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String worldName = player.getWorld().getName();

        scoreboardManager.create(player);
        swiftSidebar.setLines(player, swiftSidebarConfig.getLines(worldName));

        try {
            swiftNametag.playerJoin(player);
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
