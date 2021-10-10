package dev._2lstudios.swiftboard;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.swiftboard.listeners.PlayerJoinListener;
import dev._2lstudios.swiftboard.listeners.PlayerQuitListener;
import dev._2lstudios.swiftboard.scoreboard.Scoreboard;
import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import dev._2lstudios.swiftboard.swift.SwiftHealth;
import dev._2lstudios.swiftboard.swift.SwiftNametag;
import dev._2lstudios.swiftboard.swift.SwiftSidebar;

public class SwiftBoard extends JavaPlugin {
    private static ScoreboardManager scoreboardManager;

    public static ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        scoreboardManager = new ScoreboardManager();

        for (final Player player : getServer().getOnlinePlayers()) {
            scoreboardManager.create(player);
        }

        final SwiftHealth swiftHealth = new SwiftHealth(this, scoreboardManager);
        final SwiftSidebar swiftSidebar = new SwiftSidebar(this, scoreboardManager);
        final SwiftNametag swiftNametag = new SwiftNametag(this, scoreboardManager);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(swiftSidebar, scoreboardManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(scoreboardManager), this);

        getServer().getScheduler().runTaskTimer(this, swiftHealth, 1L, 1L);
        getServer().getScheduler().runTaskTimer(this, swiftSidebar, 1L, 1L);
        getServer().getScheduler().runTaskTimer(this, swiftNametag, 1L, 1L);
    }

    @Override
    public void onDisable() {
        try {
            for (final Scoreboard scoreboard : scoreboardManager.getScoreboards()) {
                scoreboard.clearObjectives();
                scoreboard.clearTeams();
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}