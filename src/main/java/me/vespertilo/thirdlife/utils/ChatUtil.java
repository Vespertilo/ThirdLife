package me.vespertilo.thirdlife.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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

    public static void sendGlobalTitle(String title, String subtitle, int ticks) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendTitle(p, title, subtitle, ticks);
        }
    }

    public static void sendExclusiveTitle(List<Player> players, String title, String subtitle, int ticks) {
        for (Player p : players) {
            sendTitle(p, title, subtitle, ticks);
        }
    }

    public static void sendTitle(Player p, String title, String subtitle, int ticks) {
        p.sendTitle(ChatUtil.colorize(title), ChatUtil.colorize(subtitle), 1, ticks, 1);
    }

    public static void playGlobalSound(Sound sound, float volume, float pitch) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }

    public static void playExclusiveSound(List<Player> players, Sound sound, float volume, float pitch) {
        for (Player p : players) {
            p.playSound(p.getLocation(), sound, volume, pitch);
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
