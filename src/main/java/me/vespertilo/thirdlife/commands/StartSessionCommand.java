package me.vespertilo.thirdlife.commands;

import me.vespertilo.thirdlife.SessionManager;
import me.vespertilo.thirdlife.ThirdLife;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartSessionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SessionManager.start(ThirdLife.getInstance());
        return false;
    }
}
