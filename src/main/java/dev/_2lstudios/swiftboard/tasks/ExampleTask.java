package dev._2lstudios.swiftboard.tasks;

import org.bukkit.Bukkit;

import dev._2lstudios.swiftboard.SwiftBoard;

public class ExampleTask implements Runnable {
    @Override
    public void run() {
        final String message = SwiftBoard.getInstance().getConfig().getString("messages.from-task");
        Bukkit.getServer().broadcastMessage(message);
    }
}