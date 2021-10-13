package dev._2lstudios.swiftboard.swift.config;

import org.bukkit.configuration.ConfigurationSection;

public class SwiftHealthConfig {
    private boolean enabled;
    private int updateTicks;
    private String icon;

    public void update(final ConfigurationSection section) {
        this.enabled = section.getBoolean("enabled");
        this.updateTicks = section.getInt("update-ticks");
        this.icon = section.getString("icon");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getUpdateTicks() {
        return updateTicks;
    }

    public String getIcon() {
        return icon;
    }
}
