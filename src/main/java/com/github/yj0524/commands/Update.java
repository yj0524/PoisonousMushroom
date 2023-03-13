package com.github.yj0524.commands;

import com.github.yj0524.Main;
import com.github.yj0524.util.UpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Update implements CommandExecutor {

    Main main;

    public Update(JavaPlugin main) {
        this.main = (Main) main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // arg[0]이 없을 경우
        if (args.length != 1) {
            sender.sendMessage("§c사용법 : /update <check>");
        }
        else if (args.length == 1) {
            // sender가 console일 경우에 서버 업데이트 확인
            if (sender instanceof ConsoleCommandSender) {
                UpdateChecker.check(main, "yj0524", "PoisonousMushroom");
            }
            else {
                sender.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
            }
        }
        return true;
    }
}
