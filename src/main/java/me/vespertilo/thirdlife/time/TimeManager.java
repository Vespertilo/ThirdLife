package me.vespertilo.thirdlife.time;

import me.vespertilo.thirdlife.ThirdLife;
import me.vespertilo.thirdlife.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class TimeManager {

    private final ThirdLife thirdLife;
    private final TimeDisplay timeDisplay;

    private HashMap<UUID, Integer> cachedTimes;
    private BukkitTask timer;

    public TimeManager(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
        this.timeDisplay = new TimeDisplay();
    }

    public void loadTimeMap() {
        cachedTimes = thirdLife.configManager.getTimeHashmap(thirdLife.configManager.getTimeConfig());
    }

    public void setCachedTimes(HashMap<UUID, Integer> map) {
        this.cachedTimes = map;
    }

    public HashMap<UUID, Integer> getCachedTimes() {
        return this.cachedTimes;
    }

    public void addTime(Player p, int minutes, boolean sayHours) {
        UUID uuid = p.getUniqueId();
        int time = getCachedTime(p);

        time += minutes * 60;

        cachedTimes.put(uuid, time);
        if (sayHours) {
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

        cachedTimes.put(uuid, time);
        if (sayHours) {
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
        return cachedTimes.get(uuid);
    }

    public void startTimerTick() {
        timer = new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : cachedTimes.keySet()) {
                    int time = getCachedTime(uuid);
                    time--;
                    cachedTimes.put(uuid, time);

                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
//                        timeDisplay.updateActionbarDisplay(p,  time);
                        timeDisplay.updatePlayerDisplay(p, time);
//                        updateBoard(uuid);
                        if (time <= 0) {
                            p.setGameMode(GameMode.SPECTATOR);
                            p.getWorld().strikeLightningEffect(p.getLocation());
                            ChatUtil.sendTitle(p, "&cYou ran out of time.", "&cSkill Issue", 60);
                            ChatUtil.sendGlobalMessage("&c" + p.getName() + " ran out of time.");
                            ChatUtil.playGlobalSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
                            cachedTimes.remove(uuid);
                        }
                    }
                }
            }
        }.runTaskTimer(thirdLife, 20L, 20L);
    }

    public void endTimerTick() {
        timer.cancel();
    }
}
