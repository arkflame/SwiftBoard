package dev._2lstudios.swiftboard.scoreboard;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

public class ScoreboardManager {
    private final Map<Player, Scoreboard> scoreboards = new ConcurrentHashMap<>();

    public Scoreboard getScoreboard(final Player player) {
        return scoreboards.getOrDefault(player, null);
    }

    public void create(final Player player) {
        if (!scoreboards.containsKey(player)) {
            final Scoreboard scoreboard = new Scoreboard(player);

            scoreboards.put(player, scoreboard);

            return;
        }

        scoreboards.get(player);
    }

    public void remove(final Player player) {
        if (scoreboards.containsKey(player)) {
            final Scoreboard scoreboard = scoreboards.get(player);

            scoreboards.remove(player);

        }

    }

    public Collection<Scoreboard> getScoreboards() {
        return scoreboards.values();
    }

    public boolean hasScoreboard(final Player player) {
        return scoreboards.containsKey(player);
    }
}
