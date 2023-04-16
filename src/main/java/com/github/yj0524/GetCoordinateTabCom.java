package com.github.yj0524;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class GetCoordinateTabCom implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] arg) {
        List<String> result = new ArrayList<>();

        // arg 2개 이상 뒤에는 아무것도 띄우지 않음
        if (arg.length >= 2) {
            result.clear();
            return result;
        }

        if (arg.length == 1) {
            return null;
        }

        return arguments;
    }
}
