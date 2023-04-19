package me.vespertilo.thirdlife.utils;

public class TimeUtil {

    public static String unixToHourMinuteSecond(int timestamp) {
        int hours = timestamp / 3600;
        int minutes = (timestamp / 60) % 60;
        int seconds = timestamp % 60;
        return hours + ":" + minutes + ":" + seconds;
    }
}
