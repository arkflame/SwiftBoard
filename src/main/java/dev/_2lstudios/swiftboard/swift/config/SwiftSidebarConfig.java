package dev._2lstudios.swiftboard.swift.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

public class SwiftSidebarConfig {
    private boolean enabled;
    private int updateTicks;
    private Map<String, List<String>> worlds;

    public void update(final ConfigurationSection section) {
        final ConfigurationSection worldsSection = section.getConfigurationSection("worlds");

        this.enabled = section.getBoolean("enabled");
        this.updateTicks = section.getInt("update-ticks");
        this.worlds = new HashMap<>();

        for (final String key : worldsSection.getKeys(false)) {
            final List<String> lines = worldsSection.getStringList(key);

            worlds.put(key, lines);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getUpdateTicks() {
        return updateTicks;
    }

    public List<String> getLines(final String worldName) {
        return worlds.getOrDefault(worldName, worlds.getOrDefault("default", null));
    }
}
