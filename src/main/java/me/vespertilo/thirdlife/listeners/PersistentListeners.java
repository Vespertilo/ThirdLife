package me.vespertilo.thirdlife.listeners;

import me.vespertilo.thirdlife.time.TimeManager;
import me.vespertilo.thirdlife.SessionManager;
import me.vespertilo.thirdlife.ThirdLife;
import me.vespertilo.thirdlife.utils.ChatUtil;
import me.vespertilo.thirdlife.utils.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

import static me.vespertilo.thirdlife.ThirdLife.unix24hrs;

public class PersistentListeners implements Listener {

    private final ThirdLife thirdLife;
    private final SessionManager sessionManager;
    private final TimeManager timeManager;

    public PersistentListeners(ThirdLife thirdLife, SessionManager sessionManager, TimeManager timeManager) {
        this.thirdLife = thirdLife;
        this.sessionManager = sessionManager;
        this.timeManager = timeManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();

        Player killer = dead.getKiller();
        if (killer == null) {
            timeManager.removeTime(dead, 60, true);
            return;
        }

        if (killer.getUniqueId().equals(sessionManager.getBoogeyman().getUniqueId())) {
            if (!sessionManager.boogeyCured) {
                timeManager.addTime(killer, 60, true);
                sessionManager.cureBoogey();

                timeManager.removeTime(dead, 120, true);
            } else {
                timeManager.addTime(killer, 30, false);
                timeManager.removeTime(dead, 60, true);
            }

        } else {
            timeManager.addTime(killer, 30, false);
            timeManager.removeTime(dead, 60, true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (sessionManager.inGrace) {
                event.setCancelled(true);
            }

            if (!sessionManager.isStarted()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!sessionManager.isStarted()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!sessionManager.isStarted()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        HashMap<UUID, Integer> times = timeManager.getCachedTimes();

        if (!times.containsKey(uuid)) {
            times.put(uuid, unix24hrs);
        }

        timeManager.setCachedTimes(times);

        p.discoverRecipe(new NamespacedKey(thirdLife, "tntnew"));
        p.discoverRecipe(new NamespacedKey(thirdLife, "nametagnew"));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        HashMap<UUID, Integer> times = timeManager.getCachedTimes();

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

    }
}
