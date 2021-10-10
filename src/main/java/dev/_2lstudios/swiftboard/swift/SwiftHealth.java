package dev._2lstudios.swiftboard.swift;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.swiftboard.scoreboard.Scoreboard;
import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;

public class SwiftHealth implements Runnable {
    private final Plugin plugin;
    private final ScoreboardManager scoreboardManager;

    public SwiftHealth(final Plugin plugin, final ScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager;
    }

	@Override
	public void run() {
        try {
            for (final Scoreboard sb1 : scoreboardManager.getScoreboards()) {
                for (final Scoreboard sb2 : scoreboardManager.getScoreboards()) {
                    final Player player = sb2.getPlayer();

                    sb1.updateScore("swifthealth", player.getName(), (int) player.getHealth());
                }
            }
        } catch (final InvocationTargetException e) {
            plugin.getLogger().info("Failed to send SwiftNametag to players!");
        }
	}
}
