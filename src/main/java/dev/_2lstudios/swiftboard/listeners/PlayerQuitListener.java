package dev._2lstudios.swiftboard.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;

public class PlayerQuitListener implements Listener {
    private final ScoreboardManager scoreboardManager;

    public PlayerQuitListener(final ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        scoreboardManager.remove(event.getPlayer());
    }
}
