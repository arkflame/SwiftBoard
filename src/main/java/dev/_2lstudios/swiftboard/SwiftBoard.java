package dev._2lstudios.swiftboard;

import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.swiftboard.commands.SwiftBoardCommand;
import dev._2lstudios.swiftboard.listeners.PlayerJoinListener;
import dev._2lstudios.swiftboard.tasks.ExampleTask;

public class SwiftBoard extends JavaPlugin {
    
    @Override
    public void onEnable () {
        // Save default config
        this.saveDefaultConfig();

        // Set static instance
        SwiftBoard.instance = this;

        // Register the example command
        this.getCommand("swiftboard").setExecutor(new SwiftBoardCommand());
        
        // Register the example listener
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // Register the example task
        final long taskRepeatEvery = this.getConfig().getInt("task-repeat-every") * 20L;
        this.getServer().getScheduler().runTaskTimer(this, new ExampleTask(), taskRepeatEvery, taskRepeatEvery);
    }

    private static SwiftBoard instance;

    public static SwiftBoard getInstance () {
        return SwiftBoard.instance;
    }
}