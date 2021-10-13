package dev._2lstudios.swiftboard.swift;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.swiftboard.scoreboard.HealthDisplay;
import dev._2lstudios.swiftboard.scoreboard.Scoreboard;
import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import dev._2lstudios.swiftboard.swift.config.SwiftHealthConfig;

public class SwiftHealth implements Runnable {
    private final Plugin plugin;
    private final ScoreboardManager scoreboardManager;
    private final SwiftHealthConfig swiftHealthConfig;

    public SwiftHealth(final Plugin plugin, final ScoreboardManager scoreboardManager, final SwiftHealthConfig swiftHealthConfig) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager;
        this.swiftHealthConfig = swiftHealthConfig;
    }

    @Override
    public void run() {
        try {
            for (final Scoreboard sb1 : scoreboardManager.getScoreboards()) {
                for (final Scoreboard sb2 : scoreboardManager.getScoreboards()) {
                    final Player player = sb2.getPlayer();

                    if (!sb1.containsObjective("swifthealth")) {
                        sb1.createObjective("swifthealth", swiftHealthConfig.getIcon(), HealthDisplay.INTEGER);
                        sb1.displayObjective(2, "swifthealth");
                    }

                    sb1.updateScore("swifthealth", player.getName(), (int) player.getHealth());
                }
            }
        } catch (final InvocationTargetException e) {
            plugin.getLogger().info("Failed to send SwiftNametag to players!");
        }
    }
}
