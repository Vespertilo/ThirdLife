package me.vespertilo.thirdlife.commands;

import me.vespertilo.thirdlife.config.TimeConfig;
import me.vespertilo.thirdlife.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.vespertilo.thirdlife.ThirdLife.unix24hrs;

public class ConfigCommand implements CommandExecutor {

    private TimeConfig config;

    public ConfigCommand(TimeConfig config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            return false;
        }

        String arg1 = args[0];
        String arg2 = args[1];
        switch (arg1) {
            case "add":
                switch (arg2) {
                    case "*":
                        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                        Map<String, Integer> newTimes = config.getTimeValues();
                        for (Player p : players) {
                            newTimes.put(p.getUniqueId().toString(), unix24hrs);
                        }
                        config.setTimeValues(newTimes);
                        sender.sendMessage(ChatUtil.colorize("&aSuccess! Added all online players ("  + players.size() + ") to the config."));
                        break;
                    default:
                        Player p = Bukkit.getPlayer(arg2);
                        if(p != null && p.isOnline()) {
                            Map<String, Integer> times = config.getTimeValues();
                            times.put(p.getUniqueId().toString(), unix24hrs);

                            config.setTimeValues(times);
                            sender.sendMessage(ChatUtil.colorize("&aSuccess! Added " + p.getName() + " to the config."));
                        } else {
                            sender.sendMessage(ChatUtil.colorize("&cCould not find player."));
                        }
                        break;
                }
                break;
            default:
                break;
        }

        return false;
    }
}
