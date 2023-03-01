package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PoisonousMushroom implements CommandExecutor {

    Main main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player allPlayers = (Player) sender;
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("§c사용법 : /poisonousmushroom <gameend, vaccine>");
            }
            // 만약 arg[0]이 gameend라면 "관리자가 게임을 종료했습니다." 라는 SubTitle과 함께 게임 종료
            else if (args[0].equals("gameend")) {
                if (player.isOp()) {
                    // 모든 플레이어에게 "관리자가 게임을 종료했습니다." 라는 SubTitle을 보냄
                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        if (main.serverAutoShutDown) {
                            // serverShutDownTick 틱 후에 서버 종료
                            allplayers.sendTitle("§c게임 종료", "§a관리자가 게임을 종료했습니다." + (main.serverShutDownTick / 20) + "초 후에 서버가 종료됩니다.");
                            Bukkit.getScheduler().runTaskLater(main, () -> Bukkit.getServer().shutdown(), main.serverShutDownTick + 200);
                        } else {
                            allplayers.sendTitle("§c게임 종료", "§a관리자가 게임을 종료했습니다.");
                        }
                    }
                    // Mushroom 팀을 Spectator 팀으로 모두 Join
                    for (String entry : main.mushroomTeam.getEntries()) {
                        Player player1 = Bukkit.getPlayer(entry);
                        if (player1 != null) {
                            player1.setGameMode(GameMode.SPECTATOR);
                            main.spectatorTeam.addEntry(player1.getName());
                        }
                    }
                    // People 팀을 Spectator 팀으로 모두 Join
                    for (String entry : main.peopleTeam.getEntries()) {
                        Player player1 = Bukkit.getPlayer(entry);
                        if (player1 != null) {
                            player1.setGameMode(GameMode.SPECTATOR);
                            main.spectatorTeam.addEntry(player1.getName());
                        }
                    }
                } else {
                    player.sendMessage("§c권한이 없습니다.");
                }
            } else if (args[0].equals("vaccine")) {
                if (player.isOp()) {
                    // arg[1]이 없을 경우
                    if (args.length == 1) {
                        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                        ItemMeta totemMeta = totem.getItemMeta();
                        totemMeta.setDisplayName("§c포자 퇴치기");
                        totem.setItemMeta(totemMeta);
                        player.getInventory().addItem(totem);
                        player.sendMessage("§a포자 퇴치기를 지급했습니다.");
                    }
                    // arg[1]이 PlayerName일 경우
                    else if (args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                            ItemMeta totemMeta = totem.getItemMeta();
                            totemMeta.setDisplayName("§c포자 퇴치기");
                            totem.setItemMeta(totemMeta);
                            target.getInventory().addItem(totem);
                            if (player.getName().toString() == target.getName().toString()) {
                                player.sendMessage("§a포자 퇴치기를 지급했습니다.");
                            } else if (player.getName().toString() != target.getName().toString()) {
                                player.sendMessage("§a포자 퇴치기를 지급했습니다.");
                                target.sendMessage("§a포자 퇴치기를 지급받았습니다.");
                            }
                        } else if (target == null) {
                            player.sendMessage("§c플레이어를 찾을 수 없습니다.");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("help")) {
                player.sendMessage("§a사용법 : /poisonousmushroom <gameend, vaccine> [PlayerName (vaccine command only)]");
            }
        }
        return true;
    }
}
