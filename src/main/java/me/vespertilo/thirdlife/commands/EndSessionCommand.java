package me.vespertilo.thirdlife.commands;

import me.vespertilo.thirdlife.ThirdLife;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EndSessionCommand implements CommandExecutor {

    private final ThirdLife thirdLife;

    public EndSessionCommand(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        thirdLife.sessionManager.end();
        return false;
    }
}
