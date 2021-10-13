package dev._2lstudios.swiftboard.swift.config;

import org.bukkit.configuration.ConfigurationSection;

public class SwiftNametagConfig {
    private boolean enabled;
    private int updateTicks;
    private String prefix;
    private String suffix;

    public void update(final ConfigurationSection section) {
        this.enabled = section.getBoolean("enabled");
        this.updateTicks = section.getInt("update-ticks");
        this.prefix = section.getString("prefix");
        this.suffix = section.getString("suffix");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getUpdateTicks() {
        return updateTicks;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
