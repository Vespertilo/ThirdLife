package me.vespertilo.thirdlife.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatUtil {

    public static void sendGlobalMessage(String s) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatUtil.colorize(s));
        }
    }

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(List<String> s) {
        List<String> colored = new ArrayList<>();
        for (String ss : s) {
            colored.add(colorize(ss));
        }
        return colored;
    }

    public static List<String> colorize(String[] s) {
        return colorize(Arrays.asList(s));
    }
}
