package dev._2lstudios.swiftboard.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import dev._2lstudios.swiftboard.swift.SwiftNametag;
import dev._2lstudios.swiftboard.swift.SwiftSidebar;

public class PlayerQuitListener implements Listener {
    private final ScoreboardManager scoreboardManager;
    private final SwiftSidebar swiftSidebar;
    private final SwiftNametag swiftNametag;

    public PlayerQuitListener(final ScoreboardManager scoreboardManager, final SwiftSidebar swiftSidebar, final SwiftNametag swiftNametag) {
        this.scoreboardManager = scoreboardManager;
        this.swiftSidebar = swiftSidebar;
        this.swiftNametag = swiftNametag;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        scoreboardManager.remove(player);
        swiftSidebar.clearLines(player);
        swiftNametag.removePlayer(player);
    }
}
