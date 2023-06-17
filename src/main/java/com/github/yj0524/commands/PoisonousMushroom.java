package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class PoisonousMushroom implements CommandExecutor {

    Main main;

    public PoisonousMushroom(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player allPlayers = (Player) sender;
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("§c사용법 : /poisonousmushroom <type>");
            } else if (args[0].equals("gamestart")) {
                if (player.isOp()) {
                    for (String entry : main.spectatorTeam.getEntries()) {
                        Player player1 = Bukkit.getPlayer(entry);
                        if (player1 != null) {
                            if (player1.getName().toString() != main.mushroomPlayerName) {
                                player1.setGameMode(GameMode.SURVIVAL);
                                main.peopleTeam.addEntry(player1.getName());
                            }
                            main.mushroomTeam.addEntry(main.mushroomPlayerName);
                        }
                    }
                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        allplayers.sendTitle("§a게임 시작", "§a관리자가 게임을 시작했습니다.");
                    }
                } else {
                    player.sendMessage("§c권한이 없습니다.");
                }
            } else if (args[0].equals("gameend")) {
                if (player.isOp()) {
                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        if (main.serverAutoShutDown) {
                            // serverShutDownTick 틱 후에 서버 종료
                            allplayers.sendTitle("§c게임 종료", "§a관리자가 게임을 종료했습니다. " + (main.serverShutDownTick / 20) + "초 후에 서버가 종료됩니다.");
                            Bukkit.getScheduler().runTaskLater(main, () -> Bukkit.getServer().shutdown(), main.serverShutDownTick + 200);
                        } else {
                            allplayers.sendTitle("§c게임 종료", "§a관리자가 게임을 종료했습니다.");
                        }
                    }
                    for (String entry : main.mushroomTeam.getEntries()) {
                        Player player1 = Bukkit.getPlayer(entry);
                        if (player1 != null) {
                            player1.setGameMode(GameMode.SPECTATOR);
                            main.spectatorTeam.addEntry(player1.getName());
                        }
                    }
                    for (String entry : main.peopleTeam.getEntries()) {
                        Player player1 = Bukkit.getPlayer(entry);
                        if (player1 != null) {
                            player1.setGameMode(GameMode.SPECTATOR);
                        }
                    }
                    main.isGameEnd = true;
                } else {
                    player.sendMessage("§c권한이 없습니다.");
                }
            } else if (args[0].equals("vaccine")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                        ItemMeta totemMeta = totem.getItemMeta();
                        totemMeta.setDisplayName("§c포자 퇴치기");
                        totem.setItemMeta(totemMeta);
                        player.getInventory().addItem(totem);
                        player.sendMessage("§a포자 퇴치기를 지급했습니다.");
                    }
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
            } else if (args[0].equals("respawnsemaphore")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        ItemStack totem = new ItemStack(Material.HEART_OF_THE_SEA);
                        ItemMeta totemMeta = totem.getItemMeta();
                        totemMeta.setDisplayName("§a부활 신호기");
                        totem.setItemMeta(totemMeta);
                        player.getInventory().addItem(totem);
                        player.sendMessage("§a부활 신호기를 지급했습니다.");
                    }
                    else if (args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            ItemStack totem = new ItemStack(Material.HEART_OF_THE_SEA);
                            ItemMeta totemMeta = totem.getItemMeta();
                            totemMeta.setDisplayName("§a부활 신호기");
                            totem.setItemMeta(totemMeta);
                            target.getInventory().addItem(totem);
                            if (player.getName().toString() == target.getName().toString()) {
                                player.sendMessage("§a부활 신호기를 지급했습니다.");
                            } else if (player.getName().toString() != target.getName().toString()) {
                                player.sendMessage("§a부활 신호기를 지급했습니다.");
                                target.sendMessage("§a부활 신호기를 지급받았습니다.");
                            }
                        } else if (target == null) {
                            player.sendMessage("§c플레이어를 찾을 수 없습니다.");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("forcehuskspawn")) {
                if (player.isOp()) {
                    for (String entry : main.peopleTeam.getEntries()) {
                        Player player1 = Bukkit.getPlayer(entry);
                        if (player1 != null) {
                            for (int i = 0; i < main.huskCount; i++) {
                                Location loc = player1.getLocation().add(Math.random() * 30, 0, Math.random() * 30);
                                Husk husk = (Husk) loc.getWorld().spawnEntity(loc, EntityType.HUSK);
                                husk.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(main.huskHealth);
                                husk.setHealth(main.huskHealth);
                            }
                        }
                    }
                    player.sendMessage("§a허스크를 소환했습니다.");
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("mushroomappear")) {
                if (player.isOp()) {
                    main.isTimerStart = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!main.isTimerStart) {
                                this.cancel();
                            }

                            for (Player allplayer : Bukkit.getOnlinePlayers()) {
                                if (main.mushroomAppearSeconds == 0) {
                                    if (main.mushroomAppearMinutes == 0) {
                                        allplayer.sendMessage("§c버섯이 출몰합니다!");
                                        allplayer.playSound(allplayer.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.3f, 1);

                                        for (String entry : main.mushroomTeam.getEntries()) {
                                            Player player1 = Bukkit.getPlayer(entry);
                                            player1.setGameMode(GameMode.SURVIVAL);
                                        }

                                        this.cancel();
                                    } else {
                                        main.mushroomAppearMinutes--;
                                        main.mushroomAppearSeconds = 59;
                                        main.configSave();
                                        main.saveConfig();
                                    }
                                } else {
                                    main.mushroomAppearSeconds--;
                                    main.configSave();
                                    main.saveConfig();
                                }

                                if (allplayer != null) {
                                    if (main.mushroomAppearMinutes == 0 && main.mushroomAppearSeconds == 0) {
                                        allplayer.sendActionBar("§c버섯이 출몰합니다!");
                                    } else if (main.mushroomAppearSeconds >= 0 && main.mushroomAppearSeconds <= 9 && main.mushroomAppearMinutes >= 0 && main.mushroomAppearMinutes <= 9) {
                                        allplayer.sendActionBar("§c0" + main.mushroomAppearMinutes + " : 0" + main.mushroomAppearSeconds + " 후에 버섯이 출몰합니다!");
                                    } else if (main.mushroomAppearSeconds >= 0 && main.mushroomAppearSeconds <= 9) {
                                        allplayer.sendActionBar("§c" + main.mushroomAppearMinutes + " : 0" + main.mushroomAppearSeconds + " 후에 버섯이 출몰합니다!");
                                    } else if (main.mushroomAppearMinutes >= 0 && main.mushroomAppearMinutes <= 9) {
                                        allplayer.sendActionBar("§c0" + main.mushroomAppearMinutes + " : " + main.mushroomAppearSeconds + " 후에 버섯이 출몰합니다!");
                                    } else if (main.mushroomAppearMinutes > 0 && main.mushroomAppearSeconds > 0) {
                                        allplayer.sendActionBar("§c" + main.mushroomAppearMinutes + " : " + main.mushroomAppearSeconds + " 후에 버섯이 출몰합니다!");
                                    }
                                }
                            }
                        }
                    }.runTaskTimer(main, 0, 20);
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("timerstop")) {
                if (player.isOp()) {
                    if (!main.isTimerStart) {
                        player.sendMessage("§c타이머가 이미 멈춰있습니다.");
                    } else {
                        main.isTimerStart = false;
                        player.sendMessage("§a타이머를 멈췄습니다.");
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("timerreset")) {
                if (player.isOp()) {
                    main.mushroomAppearMinutes = 30;
                    main.mushroomAppearSeconds = 0;
                    main.configSave();
                    main.saveConfig();
                    player.sendMessage("§a타이머를 초기화했습니다.");
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("timerset")) {
                if (player.isOp()) {
                    if (args.length == 3) {
                        main.mushroomAppearMinutes = Integer.parseInt(args[1]);
                        main.mushroomAppearSeconds = Integer.parseInt(args[2]);
                        main.configSave();
                        main.saveConfig();
                        player.sendMessage("§a타이머를 설정했습니다.");
                    } else if (args.length == 2) {
                        player.sendMessage("§c사용법 : /poisonousmushroom timerset <minutes> <seconds>");
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("timerget")) {
                if (player.isOp()) {
                    if (main.mushroomAppearSeconds >= 0 && main.mushroomAppearSeconds <= 9 && main.mushroomAppearMinutes >= 0 && main.mushroomAppearMinutes <= 9) {
                        player.sendActionBar("§a현재 버섯 출몰까지 남은 시간 : 0" + main.mushroomAppearMinutes + "분 0" + main.mushroomAppearSeconds + "초");
                    } else if (main.mushroomAppearSeconds >= 0 && main.mushroomAppearSeconds <= 9) {
                        player.sendActionBar("§a현재 버섯 출몰까지 남은 시간 : " + main.mushroomAppearMinutes + "분 0" + main.mushroomAppearSeconds + "초");
                    } else if (main.mushroomAppearMinutes >= 0 && main.mushroomAppearMinutes <= 9) {
                        player.sendActionBar("§a현재 버섯 출몰까지 남은 시간 : 0" + main.mushroomAppearMinutes + "분 " + main.mushroomAppearSeconds + "초");
                    } else if (main.mushroomAppearMinutes > 0 && main.mushroomAppearSeconds > 0) {
                        player.sendActionBar("§a현재 버섯 출몰까지 남은 시간 : " + main.mushroomAppearMinutes + "분 " + main.mushroomAppearSeconds + "초");
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("help")) {
                player.sendMessage("§a사용법 : /poisonousmushroom <type>");
            }
        }
        return true;
    }
}
