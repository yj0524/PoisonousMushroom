package com.github.yj0524;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class UtilTabCom implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] arg) {
        if (arguments.isEmpty()) {
            arguments.add("help");
            arguments.add("huskhealth");
            arguments.add("huskcount");
            arguments.add("mushroomplayername");
            arguments.add("serverautoshutdown");
            arguments.add("servershutdowntick");
            arguments.add("mobfollowrange");
            arguments.add("respawnspectatorrange");
            arguments.add("mobspawn");
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

        // huskhealth Command
        if (arg[0].equalsIgnoreCase("huskhealth")) {
            result.clear();
            return result;
        }

        // huskcount Command
        if (arg[0].equalsIgnoreCase("huskcount")) {
            result.clear();
            return result;
        }

        // mushroomplayername Command
        if (arg[0].equalsIgnoreCase("mushroomplayername")) {
            result.clear();
            return result;
        }

        // serverautoshutdown Command
        if (arg[0].equalsIgnoreCase("serverautoshutdown")) {
            result.add("true");
            result.add("false");
            return result;
        }

        // servershutdowntick Command
        if (arg[0].equalsIgnoreCase("servershutdowntick")) {
            result.clear();
            return result;
        }

        // mobfollowrange Command
        if (arg[0].equalsIgnoreCase("mobfollowrange")) {
            result.clear();
            return result;
        }

        // respawnspectatorrange Command
        if (arg[0].equalsIgnoreCase("respawnspectatorrange")) {
            result.clear();
            return result;
        }

        // mobspawn Command
        if (arg[0].equalsIgnoreCase("mobspawn")) {
            result.add("true");
            result.add("false");
            return result;
        }

        return arguments;
    }
}
