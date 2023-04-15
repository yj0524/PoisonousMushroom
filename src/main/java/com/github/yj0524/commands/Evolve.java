package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Evolve implements CommandExecutor {

    Main main;

    public Evolve(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (main.mushroomTeam.hasEntry(sender.getName())) {
            main.mushroomTeam.removeEntry(sender.getName());
            main.superMushroomTeam.addEntry(sender.getName());
            Bukkit.getPlayer(sender.getName()).getWorld().spawnEntity(Bukkit.getPlayer(sender.getName()).getLocation(), EntityType.LIGHTNING);
            for (OfflinePlayer player : main.superMushroomTeam.getPlayers()) {
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 1, false, false));
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1, false, false));
            }
            for (Player allplayers : Bukkit.getOnlinePlayers()) {
                allplayers.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sender.getName() + "이(가) 슈퍼 버섯으로 진화했습니다!");
            }
        }
        else {
            sender.sendMessage("§c권한이 없습니다.");
        }
        return false;
    }
}
