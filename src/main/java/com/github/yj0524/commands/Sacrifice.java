package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getServer;

public class Sacrifice implements CommandExecutor {

    Main main;

    public Sacrifice(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (main.peopleTeam.hasEntry(sender.getName())) {
            if (Math.random() < main.sacrificePercent / 100) {
                for (Player allPlayers : getServer().getOnlinePlayers()) {
                    allPlayers.sendMessage("§a" + sender.getName() + "님이 희생했습니다!");
                    allPlayers.sendMessage("§a" + sender.getName() + "님의 희생으로 " + main.sacrificePercent + "%의 확률로 삼지창이 바닥에 떨어졌습니다!");
                    allPlayers.playSound(allPlayers.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    main.sacrificeTeam.addEntry(sender.getName());
                    ((Player) sender).setGameMode(GameMode.SPECTATOR);
                }

                Player player = (Player) sender;
                Location location = player.getLocation();
                World world = player.getWorld();
                world.dropItem(location, new ItemStack(Material.TRIDENT));
            }
            else {
                for (Player allPlayers : getServer().getOnlinePlayers()) {
                    allPlayers.sendMessage("§a" + sender.getName() + "님이 희생했습니다!");
                    allPlayers.sendMessage("§c" + sender.getName() + "님이 " + main.sacrificePercent + "%의 확률로 삼지창을 떨어뜨리려 했지만, 실패했습니다.");
                    allPlayers.playSound(allPlayers.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                    main.sacrificeTeam.addEntry(sender.getName());
                    ((Player) sender).setGameMode(GameMode.SPECTATOR);
                }
            }
        }
        else {
            sender.sendMessage("§c당신은 이 명령어를 사용할 수 없습니다.");
        }
        return false;
    }
}
