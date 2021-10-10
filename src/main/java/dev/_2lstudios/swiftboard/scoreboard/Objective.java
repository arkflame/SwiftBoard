package dev._2lstudios.swiftboard.scoreboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Objective {
    private final Map<String, Integer> scores = new ConcurrentHashMap<>();
    private final String name;
    private String displayName;
    private int position;

    public Objective(final String name, final String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    protected void updateScore(final String entity, final Integer score) {
        if (score == null) {
            scores.remove(entity);
        } else {
            scores.put(entity, score);
        }
    }

    public boolean hasScore(final String entity) {
        return scores.containsKey(entity);
    }
    
    public Integer getScore(final String entity) {
        return scores.getOrDefault(entity, null);
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPosition() {
        return position;
    }

    protected void setPosition(final int position) {
        this.position = position;
    }

    protected void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
}
