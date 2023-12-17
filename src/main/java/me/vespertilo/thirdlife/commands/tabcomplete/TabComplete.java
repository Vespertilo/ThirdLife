package me.vespertilo.thirdlife.commands.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabComplete implements TabCompleter {

    List<String> arguments = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String a : args) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    return Arrays.asList("add", "remove");
                }
            }
            return result;
        }
        if (args.length == 2) {
            for (String a : args) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    return List.of("<number>");
                }
            }
            return result;
        }
        if (args.length == 3) {
            for (String a : args) {
                if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                    return null;
                }
            }
        }

        return null;
    }

}
