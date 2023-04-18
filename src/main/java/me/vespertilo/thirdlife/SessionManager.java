package me.vespertilo.thirdlife;

import me.vespertilo.thirdlife.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SessionManager {

    private static SessionManager sessionManager;
    private ThirdLife thirdLife;

    private boolean started;
    private Player boogeyman;

    private SessionManager(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
    }


    public void startBoogeymanCountdown(int initialMinutes, int secondaryMinutes) {
        long initialDelay = (initialMinutes * 20L) * 60;
        long secondaryDelay = (secondaryMinutes * 20L) * 60;

        ChatUtil.sendGlobalMessage("&cThe boogeyman is going to be chosen in " + (initialMinutes + secondaryMinutes) + " minutes.");

        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                ChatUtil.sendGlobalMessage(("&cThe boogeyman is going to be chosen in " + secondaryMinutes + " minutes."));
                //playSound(Sound.BUTTON_CLICK, p.getLocation(), 1f, 1f);
                BukkitTask runnable2 = new BukkitRunnable() {
                    @Override
                    public void run() {
                        ChatUtil.sendGlobalMessage("&cThe boogeyman is about to be chosen.");
                        BukkitTask runnable3 = new BukkitRunnable() {
                            @Override
                            public void run() {
                                boogeyman = chooseBoogeyman();
                                ChatUtil.sendGlobalMessage(boogeyman.getName());
                            }
                        }.runTaskLater(thirdLife, 20L * 10);
                    }
                }.runTaskLater(thirdLife, secondaryDelay);
            }
        }.runTaskLater(thirdLife, initialDelay);
    }

    public Player chooseBoogeyman() {
        List<Player> potentialPlayers = new ArrayList<>();
        for (World w : Bukkit.getWorlds()) {
            potentialPlayers.addAll(w.getPlayers());
        }

        int rand = new Random().nextInt(0, potentialPlayers.size());
        return potentialPlayers.get(rand);
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public static void start(ThirdLife thirdLife) {
        sessionManager = new SessionManager(thirdLife);
        sessionManager.setStarted(true);
        sessionManager.startBoogeymanCountdown(0, 0);
    }

    public Player getBoogeyman() {
        return boogeyman;
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }


}
