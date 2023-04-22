package me.vespertilo.thirdlife.commands;

import me.vespertilo.thirdlife.ThirdLife;
import me.vespertilo.thirdlife.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {

    private ThirdLife thirdLife;

    public TimeCommand(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            return false;
        }

        String arg1 = args[0];
        String arg2 = args[1];
        String arg3 = args[2];

        int timeAmount;

        try {
            timeAmount = Integer.parseInt(arg2);
        } catch (NumberFormatException ex) {
            sender.sendMessage(ChatUtil.colorize("&cInvalid amount of time!"));
            return false;
        }

        Player target = Bukkit.getPlayer(arg3);

        if (target == null) {
            sender.sendMessage(ChatUtil.colorize("&cInvalid player!"));
            return false;
        }

        boolean hours = timeAmount >= 60;

        switch (arg1) {
            case "add" -> {
                thirdLife.scoreboardManager.addTime(target, timeAmount, hours);
                sender.sendMessage(ChatUtil.colorize("&aAdded " + timeAmount + " minutes to " + target.getName() + "."));
            }
            case "remove" -> {
                thirdLife.scoreboardManager.removeTime(target, timeAmount, hours);
                sender.sendMessage(ChatUtil.colorize("&cRemoved " + timeAmount + " minutes from " + target.getName() + "."));
            }
        }
        return false;
    }
}
