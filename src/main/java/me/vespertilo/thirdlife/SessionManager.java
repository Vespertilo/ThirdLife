package me.vespertilo.thirdlife;

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

    private SessionManager(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
    }


    public void startBoogeymanCountdown(long ticks) {
        long shortWait = (long) (ticks * 0.05);
        long longWait = ticks - shortWait;

        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                //sendChatMesssage("&cThe boogeyman is about to be chosen...");
                //playSound(Sound.BUTTON_CLICK, p.getLocation(), 1f, 1f);
                BukkitTask runnable2 = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Player boogeyman = chooseBoogeyman();
                    }
                }.runTaskLater(thirdLife, shortWait);
            }
        }.runTaskLater(thirdLife, longWait);
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
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }


}
