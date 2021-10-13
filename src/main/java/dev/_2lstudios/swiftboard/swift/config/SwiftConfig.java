package dev._2lstudios.swiftboard.swift.config;

import org.bukkit.configuration.Configuration;

public class SwiftConfig {
    private final SwiftSidebarConfig swiftSidebarConfig;
    private final SwiftNametagConfig swiftNametagConfig;
    private final SwiftHealthConfig swiftHealthConfig;

    public SwiftConfig() {
        this.swiftSidebarConfig = new SwiftSidebarConfig();
        this.swiftNametagConfig = new SwiftNametagConfig();
        this.swiftHealthConfig = new SwiftHealthConfig();
    }

    public SwiftConfig update(final Configuration config) {
        swiftSidebarConfig.update(config.getConfigurationSection("sidebar"));
        swiftNametagConfig.update(config.getConfigurationSection("nametag"));
        swiftHealthConfig.update(config.getConfigurationSection("health"));

        return this;
    }

    public SwiftSidebarConfig getSwiftSidebarConfig() {
        return swiftSidebarConfig;
    }

    public SwiftNametagConfig getSwiftNametagConfig() {
        return swiftNametagConfig;
    }

    public SwiftHealthConfig getSwiftHealthConfig() {
        return swiftHealthConfig;
    }
}
