package com.github.yj0524;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCom implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] arg) {
        if (arguments.isEmpty()) {
            arguments.add("help");
            arguments.add("gamestart");
            arguments.add("gameend");
            arguments.add("vaccine");
            arguments.add("respawnsemaphore");
            arguments.add("forcehuskspawn");
            arguments.add("mushroomappear");
            arguments.add("timerstop");
            arguments.add("timerreset");
            arguments.add("timerset");
            arguments.add("timerget");
        }

        List<String> result = new ArrayList<>();

        if (arg.length == 1) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(arg[0].toLowerCase())) {
                    result.add(a);
                }
            }
            return result;
        }

        // arg 3개 이상 뒤에는 아무것도 띄우지 않음
        if (arg.length >= 3) {
            result.clear();
            return result;
        }

        // help Command
        if (arg[0].equalsIgnoreCase("help")) {
            result.clear();
            return result;
        }

        // gamestart Command
        if (arg[0].equalsIgnoreCase("gamestart")) {
            result.clear();
            return result;
        }

        // gameend Command
        if (arg[0].equalsIgnoreCase("gameend")) {
            result.clear();
            return result;
        }

        // vaccine Command
        if (arg[0].equalsIgnoreCase("vaccine")) {
            return null;
        }

        // respawnsemaphore Command
        if (arg[0].equalsIgnoreCase("respawnsemaphore")) {
            return null;
        }

        // forcehuskspawn Command
        if (arg[0].equalsIgnoreCase("forcehuskspawn")) {
            result.clear();
            return result;
        }

        // mushroomappear Command
        if (arg[0].equalsIgnoreCase("mushroomappear")) {
            result.clear();
            return result;
        }

        // timerstop Command
        if (arg[0].equalsIgnoreCase("timerstop")) {
            result.clear();
            return result;
        }

        // timerreset Command
        if (arg[0].equalsIgnoreCase("timerreset")) {
            result.clear();
            return result;
        }

        // timerset Command
        if (arg[0].equalsIgnoreCase("timerset")) {
            result.clear();
            return result;
        }

        // timerget Command
        if (arg[0].equalsIgnoreCase("timerget")) {
            result.clear();
            return result;
        }

        return arguments;
    }
}
