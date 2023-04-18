package me.vespertilo.thirdlife.listeners;

import me.vespertilo.thirdlife.SessionManager;
import me.vespertilo.thirdlife.ThirdLife;
import me.vespertilo.thirdlife.config.TimeConfig;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Time;
import java.util.Map;
import java.util.UUID;

import static me.vespertilo.thirdlife.ThirdLife.unix24hrs;

public class PersistentListeners implements Listener {

    private ThirdLife thirdLife;
    private SessionManager sessionManager;

    public PersistentListeners(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
        this.sessionManager = SessionManager.getSessionManager();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();

        Player killer = dead.getKiller();
        if (killer == null) {
            return;
        }

        if (killer.getUniqueId().equals(sessionManager.getBoogeyman().getUniqueId())) {
            //add time to killer
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TimeConfig config = thirdLife.timeConfig;

        Player p = event.getPlayer();
        String uuid = p.getUniqueId().toString();

        Map<String, Integer> times = config.getTimeValues();

        if (!times.containsKey(uuid)) {
            times.put(uuid, unix24hrs);
        }

        config.setTimeValues(times);
    }
}
