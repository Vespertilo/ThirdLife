package me.vespertilo.thirdlife.commands;

import me.vespertilo.thirdlife.ThirdLife;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartSessionCommand implements CommandExecutor {

    private ThirdLife thirdLife;

    public StartSessionCommand(ThirdLife thirdLife) {
        this.thirdLife = thirdLife;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1) {
            String graceStr = args[0];
            boolean grace = Boolean.parseBoolean(graceStr);
            thirdLife.sessionManager.start(ThirdLife.getInstance(), grace);
        } else {
            thirdLife.sessionManager.start(ThirdLife.getInstance(), false);
        }
        return false;
    }
}
