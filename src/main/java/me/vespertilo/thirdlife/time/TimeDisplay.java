package me.vespertilo.thirdlife.time;

import me.vespertilo.thirdlife.utils.TimeUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class TimeDisplay {

    private ScoreboardManager manager = Bukkit.getScoreboardManager();
    private Scoreboard board;
    private Team green;
    private Team yellow;
    private Team red;
    private Team dead;

    public TimeDisplay() {
        board = manager.getNewScoreboard();
        green = board.registerNewTeam("green_n");
        green.setColor(ChatColor.GREEN);
        yellow = board.registerNewTeam("yellow_n");
        yellow.setColor(ChatColor.YELLOW);
        red = board.registerNewTeam("red_n");
        red.setColor(ChatColor.RED);
        dead = board.registerNewTeam("dead_n");
        dead.setColor(ChatColor.GRAY);
    }

    public void updatePlayerDisplay(Player p, Integer time) {
        updateTeamDisplay(p, time);
        updateActionbarDisplay(p, time);
    }

    public void updateTeamDisplay(Player p, Integer time) {
        p.setScoreboard(board);
        ChatColor color = TimeUtil.getTimeColor(time);
        switch (color) {
            case GREEN -> {
                green.addEntry(p.getName());
            }
            case YELLOW -> {
                yellow.addEntry(p.getName());
            }
            case RED -> {
                red.addEntry(p.getName());
            }
            case WHITE -> {
                dead.addEntry(p.getName());
            }
        }
    }

    public void updateActionbarDisplay(Player p, Integer time) {
        ChatColor color = TimeUtil.getTimeColor(time);

        String message = color + TimeUtil.unixToHourMinuteSecond(time);

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
