package me.sammwy.example.tasks;

import org.bukkit.Bukkit;

import me.sammwy.example.ExamplePlugin;

public class ExampleTask implements Runnable {
    @Override
    public void run() {
        final String message = ExamplePlugin.getInstance().getConfig().getString("messages.from-task");
        Bukkit.getServer().broadcastMessage(message);
    }
}