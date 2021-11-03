package dev._2lstudios.swiftboard.swift;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Hex {
    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}}");

    public static String format(final Player, String text) {
        if (Bukkit.getVersion().contains("1.16")) {
            Matcher match = HEX_PATTERN.matcher(text);

            while (match.find()) {
                String color = text.substring(match.start(), match.end());
                text = text.replace(color, ChatColor.of(color) + "");
                match = HEX_PATTERN.matcher(text);
            }
        }
        
        return ChatColor.translateAlternateColorCodes('&', PlaceholderAPIHook.setPlaceholders(player, text));;
    }
}
