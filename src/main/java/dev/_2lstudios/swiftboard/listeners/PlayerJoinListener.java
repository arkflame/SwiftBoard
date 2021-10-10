package dev._2lstudios.swiftboard.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import dev._2lstudios.swiftboard.swift.SwiftNametag;
import dev._2lstudios.swiftboard.swift.SwiftSidebar;

public class PlayerJoinListener implements Listener {
    private final ScoreboardManager scoreboardManager;
    private final SwiftSidebar swiftSidebar;
    private final SwiftNametag swiftNametag;

    public PlayerJoinListener(final ScoreboardManager scoreboardManager, final SwiftSidebar swiftSidebar, final SwiftNametag swiftNametag) {
        this.swiftSidebar = swiftSidebar;
        this.scoreboardManager = scoreboardManager;
        this.swiftNametag = swiftNametag;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        try {
            scoreboardManager.create(player);

            final List<String> lines = new ArrayList<>();

            lines.add("&b&lSwiftBoard");
            lines.add("");
            lines.add("&fUptime: &a%server_uptime%");
            lines.add("&fLevel: &a%player_level%");
            lines.add("");
            lines.add("&b2lstudios.dev");
            swiftSidebar.setLines(player, lines);
            swiftNametag.playerJoin(player);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
