package dev._2lstudios.swiftboard.listeners;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import dev._2lstudios.swiftboard.swift.SwiftNametag;
import dev._2lstudios.swiftboard.swift.SwiftSidebar;
import dev._2lstudios.swiftboard.swift.config.SwiftNametagConfig;
import dev._2lstudios.swiftboard.swift.config.SwiftSidebarConfig;

public class PlayerJoinListener implements Listener {
    private final ScoreboardManager scoreboardManager;
    private final SwiftSidebar swiftSidebar;
    private final SwiftNametag swiftNametag;
    private final SwiftNametagConfig swiftNametagConfig;
    private final SwiftSidebarConfig swiftSidebarConfig;

    public PlayerJoinListener(final ScoreboardManager scoreboardManager, final SwiftSidebar swiftSidebar,
            final SwiftNametag swiftNametag, final SwiftNametagConfig swiftNametagConfig, final SwiftSidebarConfig swiftSidebarConfig) {
        this.swiftSidebar = swiftSidebar;
        this.scoreboardManager = scoreboardManager;
        this.swiftNametag = swiftNametag;
        this.swiftNametagConfig = swiftNametagConfig;
        this.swiftSidebarConfig = swiftSidebarConfig;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String worldName = player.getWorld().getName();

        scoreboardManager.create(player);

        if (swiftSidebarConfig.isEnabled()) {
            swiftSidebar.setLines(player, swiftSidebarConfig.getLinesOrDefault(worldName));
        }

        try {
            if (swiftNametagConfig.isEnabled()) {
                swiftNametag.init(player);
            }
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
