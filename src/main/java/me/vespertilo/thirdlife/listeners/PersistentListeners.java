package me.vespertilo.thirdlife.listeners;

import me.vespertilo.thirdlife.ScoreboardManager;
import me.vespertilo.thirdlife.ThirdLife;
import me.vespertilo.thirdlife.config.ConfigHelper;
import me.vespertilo.thirdlife.utils.ChatUtil;
import me.vespertilo.thirdlife.utils.ColorUtil;
import me.vespertilo.thirdlife.utils.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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



        Player killer = dead.getKiller();
        if (killer == null) {
            return;
        }

        if (killer.getUniqueId().equals(thirdLife.sessionManager.getBoogeyman().getUniqueId())) {
            if (!thirdLife.sessionManager.boogeyCured) {
                thirdLife.scoreboardManager.addTime(killer, 60, true);
                thirdLife.sessionManager.cureBoogey();

                thirdLife.scoreboardManager.removeTime(dead, 120, true);
            } else {
                thirdLife.scoreboardManager.addTime(killer, 30, false);
                thirdLife.scoreboardManager.removeTime(dead, 60, true);
            }

        } else {
            thirdLife.scoreboardManager.addTime(killer, 30, false);
            thirdLife.scoreboardManager.removeTime(dead, 60, true);
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

        HashMap<UUID, Integer> times = scoreboardManager.getCachedTime();

        if (!times.containsKey(uuid)) {
            times.put(uuid, unix24hrs);
        }

        scoreboardManager.setCachedTime(times);
        scoreboardManager.addPlayer(p);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        ScoreboardManager scoreboardManager = thirdLife.scoreboardManager;
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        HashMap<UUID, Integer> times = scoreboardManager.getCachedTime();

        if (!times.containsKey(uuid)) {
            times.put(uuid, unix24hrs);
        } else {
            event.setCancelled(true);

            int time = times.get(uuid);
            ChatColor teamColor = TimeUtil.getTimeColor(time);

            ChatUtil.sendGlobalMessage("<" + teamColor + event.getPlayer().getName() + "&r> " + event.getMessage());
        }
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        ScoreboardManager scoreboardManager = thirdLife.scoreboardManager;

        Player p = event.getPlayer();

        scoreboardManager.removePlayer(p);
    }
}
