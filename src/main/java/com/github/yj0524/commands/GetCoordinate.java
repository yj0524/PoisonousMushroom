package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GetCoordinate implements CommandExecutor {

    Main main;

    public GetCoordinate(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c사용법 : /getcoordinate <player>");
            return true;
        }

        if (args.length == 1) {
            if (main.getServer().getPlayer(args[0]) == null) {
                sender.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
                return true;
            }
            sender.sendMessage("§a" + args[0] + "의 좌표 : " + main.getServer().getPlayer(args[0]).getLocation().getBlockX() + ", " + main.getServer().getPlayer(args[0]).getLocation().getBlockY() + ", " + main.getServer().getPlayer(args[0]).getLocation().getBlockZ());
            return true;
        }

        return false;
    }
}
