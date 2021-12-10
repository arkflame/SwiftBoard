package dev._2lstudios.swiftboard.swift;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.swiftboard.hooks.PlaceholderAPIHook;
import dev._2lstudios.swiftboard.scoreboard.Scoreboard;
import dev._2lstudios.swiftboard.scoreboard.ScoreboardManager;
import dev._2lstudios.swiftboard.swift.config.SwiftNametagConfig;

public class SwiftNametag implements Runnable {
    private final Plugin plugin;
    private final ScoreboardManager scoreboardManager;
    private final SwiftNametagConfig swiftNametagConfig;
    private final Map<Player, Nametag> nametags = new ConcurrentHashMap<>();

    public SwiftNametag(final Plugin plugin, final ScoreboardManager scoreboardManager,
            final SwiftNametagConfig swiftNametagConfig) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager;
        this.swiftNametagConfig = swiftNametagConfig;
    }

    private String setPlaceholders(final Player player, final String string) {
        return ChatColor.translateAlternateColorCodes('&', PlaceholderAPIHook.setPlaceholders(player, string));
    }

    public String getPrefix(final Player player) {
        return setPlaceholders(player, swiftNametagConfig.getPrefix());
    }

    public String getSuffix(final Player player) {
        return setPlaceholders(player, swiftNametagConfig.getSuffix());
    }

    public void removePlayer(final Player player) {
        nametags.remove(player);
    }

    public void update(final Scoreboard sb, final String playerName, final String prefix, final String suffix)
            throws InvocationTargetException {
        if (sb.containsTeam(playerName)) {
            sb.updateTeam(playerName, "", prefix, suffix);
        } else {
            sb.createTeam(playerName, "", prefix, suffix, playerName);
        }
    }

    public void update(final Player player) throws InvocationTargetException {
        final Nametag nametag = nametags.getOrDefault(player, new Nametag("", ""));
        final String prefix = getPrefix(player);
        final String suffix = getSuffix(player);

        if (nametag.update(prefix, suffix)) {
            final String playerName = player.getName();

            nametags.put(player, nametag);

            for (final Scoreboard sb : scoreboardManager.getScoreboards()) {
                update(sb, playerName, prefix, suffix);
            }
        }
    }

    public void init(final Player player) throws InvocationTargetException {
        final Scoreboard sb = scoreboardManager.getScoreboard(player);
        final String playerName = player.getName();
        final Nametag nametag = nametags.getOrDefault(player, new Nametag("", ""));
        final String prefix = getPrefix(player);
        final String suffix = getSuffix(player);

        if (prefix != null || suffix != null) {
            nametag.update(prefix, suffix);
            nametags.put(player, nametag);

            for (final Scoreboard sb1 : scoreboardManager.getScoreboards()) {
                final Player player1 = sb1.getPlayer();

                update(sb1, playerName, prefix, suffix);
                update(sb, player1.getName(), getPrefix(player1), getSuffix(player1));
            }
        }
    }

    @Override
    public void run() {
        try {
            final Collection<Scoreboard> scoreboards = scoreboardManager.getScoreboards();

            for (final Scoreboard sb : scoreboards) {
                update(sb.getPlayer());
            }
        } catch (final InvocationTargetException e) {
            plugin.getLogger().info("Failed to send SwiftNametag to players!");
        }
    }

    private static class Nametag {
        private String prefix;
        private String suffix;

        public Nametag(final String prefix, final String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public boolean updatePrefix(final String prefix) {
            if (!this.prefix.equals(prefix)) {
                this.prefix = prefix;
                return true;
            }

            return false;
        }

        public boolean updateSuffix(final String suffix) {
            if (!this.suffix.equals(suffix)) {
                this.suffix = suffix;
                return true;
            }

            return false;
        }

        public boolean update(final String prefix, final String suffix) {
            return updatePrefix(prefix) || updateSuffix(suffix);
        }
    }
}
