package dev._2lstudios.swiftboard.swift;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.swiftboard.scoreboard.HealthDisplay;
import dev._2lstudios.swiftboard.scoreboard.Scoreboard;
import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class SwiftSidebar implements Runnable {
    private final Plugin plugin;
    private final ScoreboardManager scoreboardManager;
    private final Map<Player, List<String>> currentLines = new ConcurrentHashMap<>();
    private final Map<Player, List<String>> scoreboardLines = new ConcurrentHashMap<>();

    public SwiftSidebar(final Plugin plugin, final ScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager;
    }

    public void clearLines(final Player player) {
        scoreboardLines.remove(player);
    }

    public void setLines(final Player player, final List<String> lines) {
        Collections.reverse(lines);

        scoreboardLines.put(player, lines);
    }

    private String format(final Player player, final String line) {
        return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, line));
    }

    private String sendLine(final Scoreboard scoreboard, final String line, final int index)
            throws InvocationTargetException {
        if (line.isEmpty()) {
            final StringBuilder emptyBuilder = new StringBuilder();

            for (int y = 0; y < index; y++) {
                emptyBuilder.append(ChatColor.RESET.toString());
            }

            final String formattedLine = emptyBuilder.toString();

            scoreboard.updateScore("swiftsidebar", formattedLine, index);

            return formattedLine;
        } else {
            final Player player = scoreboard.getPlayer();
            final String formattedLine = format(player, line);

            scoreboard.updateScore("swiftsidebar", formattedLine, index);

            return formattedLine;
        }
    }

    private void sendLines(final Player player) throws InvocationTargetException {
        if (scoreboardManager.hasScoreboard(player)) {
            final Scoreboard scoreboard = scoreboardManager.getScoreboard(player);

            if (scoreboardLines.containsKey(player)) {
                final List<String> lines = scoreboardLines.get(player);

                if (!lines.isEmpty()) {
                    final List<String> sentLines = new ArrayList<>();
                    final String title = format(player, lines.get(lines.size() - 1));

                    if (scoreboard.containsObjective("swiftsidebar")) {
                        if (!title.equals(scoreboard.getObjective("swiftsidebar").getDisplayName())) {
                            scoreboard.updateObjective("swiftsidebar", title, HealthDisplay.INTEGER);
                        }
                    } else {
                        scoreboard.createObjective("swiftsidebar", title, HealthDisplay.INTEGER);
                        scoreboard.displayObjective(1, "swiftsidebar");
                    }

                    try {
                        for (int i = 0; i < lines.size() - 1; i++) {
                            final String sentLine = sendLine(scoreboard, lines.get(i), i);

                            sentLines.add(sentLine);
                        }
                    } finally {
                        try {
                            if (currentLines.containsKey(player)) {
                                for (final String currentLine : currentLines.get(player)) {
                                    if (!sentLines.contains(currentLine)) {
                                        scoreboard.removeScore("swiftsidebar", currentLine);
                                    }
                                }
                            }
                        } finally {
                            currentLines.put(player, sentLines);
                        }
                    }
                } else {
                    scoreboard.removeObjective("swiftsidebar");
                }
            } else {
                scoreboard.removeObjective("swiftsidebar");
            }
        }
    }

    public void sendLines() throws InvocationTargetException {
        for (final Player player : scoreboardLines.keySet()) {
            sendLines(player);
        }
    }

    @Override
    public void run() {
        try {
            sendLines();
        } catch (final InvocationTargetException e) {
            plugin.getLogger().info("Failed to send SwiftBoard to players!");
        }
    }
}
