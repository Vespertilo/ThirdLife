package me.vespertilo.thirdlife.utils;

import org.bukkit.ChatColor;

public class TimeUtil {

    public static String unixToHourMinuteSecond(int timestamp) {
        int hours = timestamp / 3600;
        int minutes = (timestamp / 60) % 60;
        int seconds = timestamp % 60;
        return hours + ":" + minutes + ":" + seconds;
    }

    public static ChatColor getTimeColor(int time) {
        int sixteenHours = 3600 * 16;
        int eightHours = 3600 * 8;
        int zero = 0;

        if(time >= sixteenHours) {
            return ChatColor.GREEN;
        }
        if(time >= eightHours) {
            return ChatColor.YELLOW;
        }
        if(time >= zero) {
            return ChatColor.RED;
        }
        return ChatColor.WHITE;
    }
}
