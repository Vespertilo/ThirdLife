package me.vespertilo.thirdlife;

import fr.mrmicky.fastboard.FastBoard;
import me.vespertilo.thirdlife.config.ConfigHelper;
import me.vespertilo.thirdlife.utils.ChatUtil;
import me.vespertilo.thirdlife.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final ThirdLife thirdLife;

    private final HashMap<UUID, Integer> cachedTime;
    private final HashMap<UUID, FastBoard> playerTimers;

    public ScoreboardManager(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;


        cachedTime = ConfigHelper.getTimeHashmap(thirdLife.getTimeConfig());
        playerTimers = new HashMap<>();
    }

    public void addPlayer(Player p) {
        playerTimers.put(p.getUniqueId(), createTimerBoard(p));
    }

    public void removePlayer(Player p) {
        FastBoard board = playerTimers.remove(p.getUniqueId());
        board.delete();
    }

    public void addTime(Player p, int minutes, boolean sayHours) {
        UUID uuid = p.getUniqueId();
        int time = getCachedTime(p);

        time += minutes * 60;

        cachedTime.put(uuid, time);
        if(sayHours) {
            int hours = (minutes / 60);

            ChatUtil.sendTitle(p, "&a+" + hours + " hours", "", 60);
        } else {
            ChatUtil.sendTitle(p, "&a+" + minutes + " minutes", "", 60);
        }
    }

    public void removeTime(Player p, int minutes, boolean sayHours) {
        UUID uuid = p.getUniqueId();
        int time = getCachedTime(p);

        time -= minutes * 60;

        cachedTime.put(uuid, time);
        if(sayHours) {
            int hours = (minutes / 60);

            ChatUtil.sendTitle(p, "&c-" + hours + " hours", "", 20);
        } else {
            ChatUtil.sendTitle(p, "&c-" + minutes + " minutes", "", 20);
        }
    }

    public int getCachedTime(Player p) {
        return getCachedTime(p.getUniqueId());
    }

    public int getCachedTime(UUID uuid) {
        return cachedTime.get(uuid);
    }

    public FastBoard createTimerBoard(Player p) {
        FastBoard fastBoard = new FastBoard(p);
        fastBoard.updateTitle(ChatUtil.colorize("&c&lLimited Life"));
        fastBoard.updateLines(ChatUtil.colorize("&a" + TimeUtil.unixToHourMinuteSecond(getCachedTime(p))));
        return fastBoard;
    }

    public void updateBoard(Player p) {
        updateBoard(p.getUniqueId());
    }

    public void updateBoard(UUID uuid) {
        FastBoard fastBoard = playerTimers.get(uuid);
        fastBoard.updateLines(ChatUtil.colorize("&a" + TimeUtil.unixToHourMinuteSecond(getCachedTime(uuid))));
    }


    public void startTimerTick() {
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : cachedTime.keySet()) {
                    int time = getCachedTime(uuid);
                    time--;
                    cachedTime.put(uuid, time);

                    Player p = Bukkit.getPlayer(uuid);
                    if(p != null) {
                        updateBoard(uuid);
                    }
                }
            }
        }.runTaskTimer(thirdLife, 20L, 20L);
    }
}
