package me.vespertilo.thirdlife.commands;

import me.vespertilo.thirdlife.ThirdLife;
import me.vespertilo.thirdlife.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RerollBoogeyCommand implements CommandExecutor {

    private ThirdLife thirdLife;

    public RerollBoogeyCommand(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatUtil.colorize("&aStarted boogeyman countdown!"));
        thirdLife.sessionManager.rollBoogeyman();
        return false;
    }
}
