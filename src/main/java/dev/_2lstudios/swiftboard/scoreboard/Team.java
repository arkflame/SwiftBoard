package dev._2lstudios.swiftboard.scoreboard;

import java.util.List;

public class Team {
    private String teamDisplayName;
    private String prefix;
    private String suffix;
    private final List<String> entities;

    protected Team(final String teamDisplayName, final String prefix, final String suffix,
            final List<String> entities) {
        this.teamDisplayName = teamDisplayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.entities = entities;
    }

    public boolean hasChanges(final String teamDisplayName, final String prefix, final String suffix) {
        return !this.teamDisplayName.equals(teamDisplayName) || !this.prefix.equals(prefix) || !this.suffix.equals(suffix);
    }

    protected void update(final String teamDisplayName, final String prefix, final String suffix) {
        this.teamDisplayName = teamDisplayName;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    protected void addEntities(final List<String> entities) {
        entities.addAll(entities);
    }

    protected void removeEntities(final List<String> entities) {
        entities.clear();
    }

    public String getTeamDisplayName() {
        return teamDisplayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public List<String> getEntities() {
        return entities;
    }
}
