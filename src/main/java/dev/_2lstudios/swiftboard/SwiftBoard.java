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
    private static SwiftHealth swiftHealth;
    private static SwiftSidebar swiftSidebar;
    private static SwiftNametag swiftNametag;

    public static ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public static SwiftHealth getSwiftHealth() {
        return swiftHealth;
    }

    public static SwiftSidebar getSwiftSidebar() {
        return swiftSidebar;
    }

    public static SwiftNametag getSwiftNametag() {
        return swiftNametag;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        scoreboardManager = new ScoreboardManager();

        for (final Player player : getServer().getOnlinePlayers()) {
            scoreboardManager.create(player);
        }

        swiftHealth = new SwiftHealth(this, scoreboardManager);
        swiftSidebar = new SwiftSidebar(this, scoreboardManager);
        swiftNametag = new SwiftNametag(this, scoreboardManager);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(scoreboardManager, swiftSidebar, swiftNametag), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(scoreboardManager, swiftSidebar, swiftNametag), this);

        getServer().getScheduler().runTaskTimerAsynchronously(this, swiftHealth, 1L, 1L);
        getServer().getScheduler().runTaskTimerAsynchronously(this, swiftSidebar, 1L, 1L);
        getServer().getScheduler().runTaskTimerAsynchronously(this, swiftNametag, 1L, 1L);
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