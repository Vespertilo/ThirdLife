package me.vespertilo.thirdlife;

import me.vespertilo.thirdlife.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SessionManager {

    private final ThirdLife thirdLife;

    private boolean started;
    public boolean inGrace;
    private Player boogeyman;

    public boolean boogeyCured;

    public SessionManager(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
    }

    public void startTimerCountdown(int time, Runnable after) {
        new BukkitRunnable() {
            int countdown = time;

            @Override
            public void run() {
                ChatUtil.sendGlobalTitle("&a" + countdown, "", 20);
                ChatUtil.playGlobalSound(Sound.BLOCK_LEVER_CLICK, 1f, 1f);

                if (countdown <= 0) {
                    startTimers();
                    after.run();
                    this.cancel();
                }
                countdown--;
            }
        }.runTaskTimer(thirdLife, 20L, 20L);
    }

    public void startBoogeymanCountdown(int initialMinutes, int secondaryMinutes) {
        long initialDelay = (initialMinutes * 20L) * 60;
        long secondaryDelay = (secondaryMinutes * 20L) * 60;

        ChatUtil.sendGlobalMessage("&cThe boogeyman is going to be chosen in " + (initialMinutes + secondaryMinutes) + " minutes.");

        new BukkitRunnable() {
            @Override
            public void run() {
                ChatUtil.sendGlobalMessage(("&cThe boogeyman is going to be chosen in " + secondaryMinutes + " minutes."));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ChatUtil.sendGlobalMessage("&cThe boogeyman is about to be chosen.");
                        ChatUtil.playGlobalSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                rollBoogeyman();
                            }
                        }.runTaskLater(thirdLife, 20L * 5);
                    }
                }.runTaskLater(thirdLife, secondaryDelay);
            }
        }.runTaskLater(thirdLife, initialDelay);
    }

    public void startGraceCountdown(int minutes) {
        ChatUtil.sendGlobalMessage("&aThe grace period is going to end in " + minutes + " minutes.");
        inGrace = true;

        final String colors = "cea";
        new BukkitRunnable() {
            @Override
            public void run() {
                ChatUtil.sendGlobalMessage(("&aThe grace period is ending!"));

                new BukkitRunnable() {
                    int i = 3;

                    @Override
                    public void run() {
                        if (i > 0) {
                            ChatUtil.sendGlobalTitle("&" + colors.charAt(i - 1) + i, "", 30);
                            ChatUtil.playGlobalSound(Sound.BLOCK_LEVER_CLICK, 1f, 1f);
                        }
                        if (i == 0) {
                            ChatUtil.sendGlobalTitle("&cPVP is now enabled.", "", 30);
                            ChatUtil.playGlobalSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
                            inGrace = false;
                            this.cancel();
                        }

                        i--;
                    }
                }.runTaskTimer(thirdLife, 0L, 30L);
            }
        }.runTaskLater(thirdLife, (1200L) * minutes);
    }

    public void rollBoogeyman() {
        boogeyman = chooseBoogeyman();

        final String colors = "cea";
        new BukkitRunnable() {
            int i = 3;

            @Override
            public void run() {
                if (i > 0) {
                    ChatUtil.sendGlobalTitle("&" + colors.charAt(i - 1) + i, "", 30);
                    ChatUtil.playGlobalSound(Sound.BLOCK_LEVER_CLICK, 1f, 1f);
                }
                if (i == 0) {
                    ChatUtil.sendGlobalTitle("&eYou are...", "", 30);
                }
                if (i < 0) {
                    ChatUtil.sendTitle(boogeyman, "&cThe Boogeyman.", "", 60);
                    boogeyman.playSound(boogeyman.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1f, 0f);

                    List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                    players.removeIf(p -> p.getUniqueId().equals(boogeyman.getUniqueId()));

                    ChatUtil.sendExclusiveTitle(players, "&aNOT the Boogeyman.", "", 60);
                    ChatUtil.playExclusiveSound(players, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    this.cancel();
                }

                i--;
            }
        }.runTaskTimer(thirdLife, 0L, 30L);

        ChatUtil.sendGlobalMessage(boogeyman.getName());
    }

    public Player chooseBoogeyman() {
        List<Player> potentialPlayers = new ArrayList<>();
        for (World w : Bukkit.getWorlds()) {
            potentialPlayers.addAll(w.getPlayers());
        }

        int rand = new Random().nextInt(0, potentialPlayers.size());
        return potentialPlayers.get(rand);
    }

    public void cureBoogey() {
        boogeyCured = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                ChatUtil.sendTitle(boogeyman, "&cYou are cured!", "", 40);
            }
        }.runTaskLater(thirdLife, 60L);

    }

    public void startTimers() {
        this.thirdLife.scoreboardManager.startTimerTick();
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public static void start(ThirdLife thirdLife, boolean grace) {
        thirdLife.sessionManager.startTimerCountdown(3, () -> {
            thirdLife.sessionManager.setStarted(true);
            thirdLife.sessionManager.startBoogeymanCountdown(0, 0);
            if (grace) {
                thirdLife.sessionManager.startGraceCountdown(20);
            }
        });
    }

    public Player getBoogeyman() {
        return boogeyman;
    }
}
