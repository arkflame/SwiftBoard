package dev._2lstudios.swiftboard.scoreboard;

public enum HealthDisplay {
    INTEGER, HEARTS;

    public static HealthDisplay fromString(final String value, final HealthDisplay def) {
        for (final HealthDisplay healthDisplay : values()) {
            if (value.equalsIgnoreCase(healthDisplay.name())) {
                return healthDisplay;
            }
        }

        return def;
    }
}
