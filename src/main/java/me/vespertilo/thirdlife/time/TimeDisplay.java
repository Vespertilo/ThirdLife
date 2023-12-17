package me.vespertilo.thirdlife.time;

import me.vespertilo.thirdlife.utils.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TimeDisplay {

    public void displayTime(Player p, Integer time) {
        ChatColor color = TimeUtil.getTimeColor(time);

        String message = color + TimeUtil.unixToHourMinuteSecond(time);

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
