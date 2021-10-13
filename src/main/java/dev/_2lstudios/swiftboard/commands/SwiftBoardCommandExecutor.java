package dev._2lstudios.swiftboard.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev._2lstudios.swiftboard.SwiftBoard;

public class SwiftBoardCommandExecutor implements CommandExecutor {
    private final SwiftBoard swiftBoard;

    public SwiftBoardCommandExecutor(final SwiftBoard swiftBoard) {
        this.swiftBoard = swiftBoard;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label,
            final String[] args) {
        if (args.length > 0) {
            if (args[0].equals("reload")) {
                swiftBoard.reloadConfig();
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid argument: " + args[0]));
            }
        } else {
            final StringBuilder helpMessageBuilder = new StringBuilder();

            helpMessageBuilder.append("&aSwiftBoard commands:");
            helpMessageBuilder.append("&e%label% reload&7 - &bReload the configuration");
            helpMessageBuilder.append("&7&oSwiftBoard was made by 2LStudios");

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    helpMessageBuilder.toString().replace("%label%", label)));
        }

        return true;
    }
}
