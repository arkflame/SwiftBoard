package dev._2lstudios.swiftboard.swift;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.swiftboard.scoreboard.Scoreboard;
import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;

public class SwiftNametag implements Runnable {
    private final Plugin plugin;
    private final ScoreboardManager scoreboardManager;

    public SwiftNametag(final Plugin plugin, final ScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public void run() {
        final String prefix = ChatColor.translateAlternateColorCodes('&', "&a");

        try {
            final Collection<Scoreboard> scoreboards = scoreboardManager.getScoreboards();

            for (final Scoreboard sb1 : scoreboards) {
                final String playerName = sb1.getPlayer().getName();

                for (final Scoreboard sb2 : scoreboards) {
                    if (sb2.containsTeam(playerName)) {
                        sb2.updateTeam(playerName, "", prefix, "");
                    } else {
                        sb2.createTeam(playerName, "", prefix, "", playerName);
                    }
                }
            }
        } catch (final InvocationTargetException e) {
            plugin.getLogger().info("Failed to send SwiftNametag to players!");
        }
    }
}
