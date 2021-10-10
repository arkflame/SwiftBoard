package dev._2lstudios.swiftboard.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.swiftboard.scoreboard.HealthDisplay;
import dev._2lstudios.swiftboard.scoreboard.Scoreboard;
import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import dev._2lstudios.swiftboard.swift.SwiftSidebar;

public class PlayerJoinListener implements Listener {
    private final SwiftSidebar swiftSidebar;
    private final ScoreboardManager scoreboardManager;

    public PlayerJoinListener(final SwiftSidebar swiftSidebar, final ScoreboardManager scoreboardManager) {
        this.swiftSidebar = swiftSidebar;
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        try {
            final Scoreboard scoreboard = scoreboardManager.create(player);

            scoreboard.createObjective("swiftnametag", "swiftnametag", HealthDisplay.INTEGER);
            scoreboard.displayObjective(0, "swiftnametag");

            final List<String> lines = new ArrayList<>();

            lines.add("&b&lSwiftBoard");
            lines.add("");
            lines.add("&fUptime: &a%server_uptime%");
            lines.add("&fLevel: &a%player_level%");
            lines.add("");
            lines.add("&b2lstudios.dev");
            swiftSidebar.setLines(player, lines);

            scoreboard.createObjective("swifthealth", ChatColor.RED + "❤", HealthDisplay.INTEGER);
            scoreboard.displayObjective(2, "swifthealth");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
