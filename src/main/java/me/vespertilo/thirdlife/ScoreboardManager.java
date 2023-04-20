package me.vespertilo.thirdlife;

import fr.mrmicky.fastboard.FastBoard;
import me.vespertilo.thirdlife.config.ConfigHelper;
import me.vespertilo.thirdlife.utils.ChatUtil;
import me.vespertilo.thirdlife.utils.ColorUtil;
import me.vespertilo.thirdlife.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

import static me.vespertilo.thirdlife.ThirdLife.unix24hrs;

public class ScoreboardManager {

    private final ThirdLife thirdLife;

    private HashMap<UUID, Integer> cachedTime;
    private final HashMap<UUID, FastBoard> playerTimers;

    private final ChatColor green = ChatColor.GREEN;
    private final ChatColor yellow = ChatColor.YELLOW;
    private final ChatColor red = ChatColor.RED;

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

    public void setCachedTime(HashMap<UUID, Integer> map) {
        this.cachedTime = map;
    }

    public HashMap<UUID, Integer> getCachedTime() {
        return this.cachedTime;
    }

    public void addTime(Player p, int minutes, boolean sayHours) {
        UUID uuid = p.getUniqueId();
        int time = getCachedTime(p);

        time += minutes * 60;

        cachedTime.put(uuid, time);
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

        cachedTime.put(uuid, time);
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
        return cachedTime.get(uuid);
    }

    public FastBoard createTimerBoard(Player p) {
        //        fastBoard.updateTitle(ChatUtil.colorize("&c&lLimited Life"));
//        fastBoard.updateLines(ChatUtil.colorize("&a" + TimeUtil.unixToHourMinuteSecond(getCachedTime(p))));
        return new FastBoard(p);
    }

    public void updateBoard(UUID uuid) {
        FastBoard fastBoard = playerTimers.get(uuid);

        Player p = Bukkit.getPlayer(uuid);

        int time = getCachedTime(uuid);

        if (time <= 0) {
            fastBoard.updateLines("&00:00:00");
            return;
        }

        //green
        int darkGreen = 0x00AA00;
        int lightGreen = 0x55FF55;

        //yellow
        int lightYellow = 0xFFFF55;
        int darkYellow = 0xFFAA00;

        //red
        int lightRed = 0xFF5555;
        int darkRed = 0xAA0000;

        // < 0.66 = yellow, < 0.3 = red
        float pcnt = (float) time / (float) unix24hrs;

        float twoThirds = (2f / 3f);
        float oneThird = (1f / 3f);

        int lerped = 0;
        if (pcnt <= 1f) {
            float i = normalize01(1 - pcnt, 0f, 0.33f);
            lerped = ColorUtil.lerpColor(darkGreen, lightGreen, i);
            p.setPlayerListName(green + p.getName());
        }
        if (pcnt <= twoThirds) {
            float i = normalize01(1 - pcnt, 0.33f, 0.66f);
            lerped = ColorUtil.lerpColor(lightYellow, darkYellow, i);
            p.setPlayerListName(yellow + p.getName());
            p.setDisplayName(yellow + p.getName());
        }
        if (pcnt <= oneThird) {
            float i = normalize01(1 - pcnt, 0.66f, 1f);
            lerped = ColorUtil.lerpColor(lightRed, darkRed, i);
            p.setPlayerListName(red + p.getName());
            p.setDisplayName(red + p.getName());
        }

        String hex = Integer.toHexString(lerped);

        if (hex.length() < 6) {
            int toAppend = 6 - hex.length();
            String empty = "0".repeat(toAppend);
            hex = empty + hex;
        }
        String color = ColorUtil.hex("#" + hex + TimeUtil.unixToHourMinuteSecond(time));

        fastBoard.updateLines(color);
    }

    float normalize01(float value, float startMin, float startMax) {
        float targetMin = 0.0f;
        float targetMax = 1.0f;
        return (((value - startMin) / (startMax - startMin)) * (targetMax - targetMin)) + targetMin;
    }

    public void startTimerTick() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : cachedTime.keySet()) {
                    int time = getCachedTime(uuid);
                    time--;
                    cachedTime.put(uuid, time);

                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
                        updateBoard(uuid);
                        if (time <= 0) {
                            p.setGameMode(GameMode.SPECTATOR);
                            p.getWorld().strikeLightningEffect(p.getLocation());
                            ChatUtil.sendTitle(p, "&cYou ran out of time.", "&cSkill Issue", 60);
                            ChatUtil.sendGlobalMessage("&c" + p.getName() + " ran out of time.");
                            ChatUtil.playGlobalSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
                            cachedTime.remove(uuid);
                        }
                    }
                }
            }
        }.runTaskTimer(thirdLife, 20L, 20L);
    }
}
