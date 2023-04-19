package me.vespertilo.thirdlife.listeners;

import me.vespertilo.thirdlife.ScoreboardManager;
import me.vespertilo.thirdlife.ThirdLife;
import me.vespertilo.thirdlife.config.ConfigHelper;
import me.vespertilo.thirdlife.utils.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.vespertilo.thirdlife.ThirdLife.unix24hrs;

public class PersistentListeners implements Listener {

    private ThirdLife thirdLife;

    public PersistentListeners(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();

        thirdLife.scoreboardManager.removeTime(dead, 60, true);

        Player killer = dead.getKiller();
        if (killer == null) {
            return;
        }

        if (killer.getUniqueId().equals(thirdLife.sessionManager.getBoogeyman().getUniqueId())) {
            if (!thirdLife.sessionManager.boogeyCured) {
                thirdLife.scoreboardManager.addTime(killer, 60, true);
                thirdLife.sessionManager.cureBoogey();
            } else {
                thirdLife.scoreboardManager.addTime(killer, 30, false);
            }

        } else {
            thirdLife.scoreboardManager.addTime(killer, 30, false);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (thirdLife.sessionManager.inGrace) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ScoreboardManager scoreboardManager = thirdLife.scoreboardManager;

        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        HashMap<UUID, Integer> times = ConfigHelper.getTimeHashmap(thirdLife.getTimeConfig());

        if (!times.containsKey(uuid)) {
            times.put(uuid, unix24hrs);
        }

        scoreboardManager.setCachedTime(times);
        scoreboardManager.addPlayer(p);

        colors(p);
    }

    private void colors(Player p) {
        int color1 = 0x00AA00;
        int color2 = 0xFFFF55;
        int color3 = 0xFF5555;

        BukkitTask task = new BukkitRunnable() {
            float i = 0;

            @Override
            public void run() {
                int lerped;
                if (i <= 1f) {
                    lerped = ColorUtil.lerpColor(color1, color2, i);
                } else if (i > 1f) {

                    lerped = ColorUtil.lerpColor(color2, color3, i - 1);
                } else {
                    lerped = 0x000000f;
                }

                String hex = Integer.toHexString(lerped);

                if (hex.length() < 6) {
                    int toAppend = 6 - hex.length();
                    String empty = "0".repeat(toAppend);
                    hex = empty + hex;
                }
                String color = ColorUtil.hex("#" + hex + p.getDisplayName());

                p.setPlayerListName(color);


                if (i > 2f) {
                    colors(p);
                    this.cancel();
                }
                i += 0.05f;
            }
        }.runTaskTimer(thirdLife, 10L, 10L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        ScoreboardManager scoreboardManager = thirdLife.scoreboardManager;

        Player p = event.getPlayer();

        scoreboardManager.removePlayer(p);
    }
}
