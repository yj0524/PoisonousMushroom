package com.github.yj0524;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AttachTabCom implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] arg) {
        if (arguments.isEmpty()) {
            arguments.add("help");
            arguments.add("people");
            arguments.add("mushroom");
            arguments.add("supermushroom");
            arguments.add("spectator");
            arguments.add("sacrifice");
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

        // people Command
        if (arg[0].equalsIgnoreCase("people")) {
            return null;
        }

        // mushroom Command
        if (arg[0].equalsIgnoreCase("mushroom")) {
            return null;
        }

        // supermushroom Command
        if (arg[0].equalsIgnoreCase("supermushroom")) {
            return null;
        }

        // spectator Command
        if (arg[0].equalsIgnoreCase("spectator")) {
            return null;
        }

        // sacrifice Command
        if (arg[0].equalsIgnoreCase("sacrifice")) {
            return null;
        }

        return arguments;
    }
}
