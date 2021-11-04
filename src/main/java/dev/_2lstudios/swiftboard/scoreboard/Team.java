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

    public boolean equals(final String str1, final String str2) {
        return (str1 == null && str2 == null) || (str1 != null && str2 != null && str1.equals(str2));
    }

    public boolean hasChanges(final String teamDisplayName, final String prefix, final String suffix) {
        return !equals(this.teamDisplayName, teamDisplayName) || !equals(this.prefix, prefix) || !equals(this.suffix, suffix);
    }

    protected void update(final String teamDisplayName, final String prefix, final String suffix) {
        this.teamDisplayName = teamDisplayName;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    protected void addEntities(final List<String> entities) {
        this.entities.addAll(entities);
    }

    protected void removeEntities(final List<String> entities) {
        this.entities.removeAll(entities);
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
