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
            arguments.add("gameend");
            arguments.add("vaccine");
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

        // arg 2개 이상 뒤에는 아무것도 띄우지 않음
        if (arg.length >= 3) {
            result.clear();
            return result;
        }

        // vaccine 뒤에는 플레이어들을 띄움
        if (arg[0].equalsIgnoreCase("vaccine")) {
            return null;
        }

        // gameend 뒤에는 아무것도 띄우지 않음
        if (arg[0].equalsIgnoreCase("gameend")) {
            result.clear();
            return result;
        }

        return arguments;
    }
}
