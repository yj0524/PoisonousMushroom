package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util implements CommandExecutor {

    Main main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("§c사용법 : /util <serverautoshutdown, servershutdowntick> [bool (serverautoshutdown command only), int (servershutdowntick command only)]");
            } else if (args[0].equals("serverautoshutdown")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a서버 자동 종료 기능은 " + main.serverAutoShutDown + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.serverAutoShutDown = true;
                            player.sendMessage("§a서버 자동 종료 기능을 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.serverAutoShutDown = false;
                            player.sendMessage("§a서버 자동 종료 기능을 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util serverautoshutdown [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("servershutdowntick")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 서버 자동 종료 시간은 " + main.serverShutDownTick + "틱입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.serverShutDownTick = Integer.parseInt(args[1]);
                            player.sendMessage("§a서버 자동 종료 시간을 " + main.serverShutDownTick + "틱으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util servershutdowntick [int]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("help")) {
                player.sendMessage("§a사용법 : /util <serverautoshutdown, servershutdowntick> [bool (serverautoshutdown command only), int (servershutdowntick command only)]");
            }
        }
        return true;
    }
}
