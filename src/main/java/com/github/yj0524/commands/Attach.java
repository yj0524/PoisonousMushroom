package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Attach implements CommandExecutor {

    Main main;

    public Attach(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equals("people")) {
            if (sender.isOp()) {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (main.peopleTeam.hasEntry(target.getName())) {
                            sender.sendMessage("§c" + target.getName() + "은(는) 이미 People 팀입니다.");
                        } else {
                            main.peopleTeam.addEntry(target.getName());
                            target.setGameMode(GameMode.SURVIVAL);
                            for (PotionEffect effect : target.getActivePotionEffects()) {
                                target.removePotionEffect(effect.getType());
                            }
                            target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            sender.sendMessage("§a" + target.getName() + "님을 People 팀에 추가했습니다.");
                        }
                    } else {
                        sender.sendMessage("§c플레이어를 찾을 수 없습니다.");
                    }
                } else {
                    if (sender instanceof Player) {
                        if (main.peopleTeam.hasEntry(sender.getName())) {
                            sender.sendMessage("§c당신은 이미 People 팀입니다.");
                        } else {
                            main.peopleTeam.addEntry(sender.getName());
                            ((Player) sender).setGameMode(GameMode.SURVIVAL);
                            for (PotionEffect effect : ((Player) sender).getActivePotionEffects()) {
                                ((Player) sender).removePotionEffect(effect.getType());
                            }
                            ((Player) sender).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            sender.sendMessage("§a당신을 People 팀에 추가했습니다.");
                        }
                    }
                }
            }
            else {
                sender.sendMessage("§c권한이 없습니다.");
            }
        }
        else if (args[0].equals("mushroom")) {
            if (sender.isOp()) {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (main.mushroomTeam.hasEntry(target.getName())) {
                            sender.sendMessage("§c" + target.getName() + "은(는) 이미 Mushroom 팀입니다.");
                        } else {
                            main.mushroomTeam.addEntry(target.getName());
                            target.setGameMode(GameMode.SURVIVAL);
                            for (PotionEffect effect : target.getActivePotionEffects()) {
                                target.removePotionEffect(effect.getType());
                            }
                            target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            sender.sendMessage("§a" + target.getName() + "님을 Mushroom 팀에 추가했습니다.");
                        }
                    } else {
                        sender.sendMessage("§c플레이어를 찾을 수 없습니다.");
                    }
                } else {
                    if (sender instanceof Player) {
                        if (main.mushroomTeam.hasEntry(sender.getName())) {
                            sender.sendMessage("§c당신은 이미 Mushroom 팀입니다.");
                        } else {
                            main.mushroomTeam.addEntry(sender.getName());
                            ((Player) sender).setGameMode(GameMode.SURVIVAL);
                            for (PotionEffect effect : ((Player) sender).getActivePotionEffects()) {
                                ((Player) sender).removePotionEffect(effect.getType());
                            }
                            ((Player) sender).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            sender.sendMessage("§a당신을 Mushroom 팀에 추가했습니다.");
                        }
                    }
                }
            }
            else {
                sender.sendMessage("§c권한이 없습니다.");
            }
        }
        else if (args[0].equals("supermushroom")) {
            if (sender.isOp()) {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (main.superMushroomTeam.hasEntry(target.getName())) {
                            sender.sendMessage("§c" + target.getName() + "은(는) 이미 SuperMushroom 팀입니다.");
                        } else {
                            main.superMushroomTeam.addEntry(target.getName());
                            target.setGameMode(GameMode.SURVIVAL);
                            for (PotionEffect effect : target.getActivePotionEffects()) {
                                target.removePotionEffect(effect.getType());
                            }
                            target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
                            sender.sendMessage("§a" + target.getName() + "님을 SuperMushroom 팀에 추가했습니다.");
                        }
                    } else {
                        sender.sendMessage("§c플레이어를 찾을 수 없습니다.");
                    }
                } else {
                    if (sender instanceof Player) {
                        if (main.superMushroomTeam.hasEntry(sender.getName())) {
                            sender.sendMessage("§c당신은 이미 SuperMushroom 팀입니다.");
                        } else {
                            main.superMushroomTeam.addEntry(sender.getName());
                            ((Player) sender).setGameMode(GameMode.SURVIVAL);
                            for (PotionEffect effect : ((Player) sender).getActivePotionEffects()) {
                                ((Player) sender).removePotionEffect(effect.getType());
                            }
                            ((Player) sender).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
                            sender.sendMessage("§a당신을 SuperMushroom 팀에 추가했습니다.");
                        }
                    }
                }
            } else {
                sender.sendMessage("§c권한이 없습니다.");
            }
        }
        else if (args[0].equals("spectator")) {
            if (sender.isOp()) {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (main.spectatorTeam.hasEntry(target.getName())) {
                            sender.sendMessage("§c" + target.getName() + "은(는) 이미 Spectator 팀입니다.");
                        } else {
                            main.spectatorTeam.addEntry(target.getName());
                            target.setGameMode(GameMode.SPECTATOR);
                            for (PotionEffect effect : target.getActivePotionEffects()) {
                                target.removePotionEffect(effect.getType());
                            }
                            target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            sender.sendMessage("§a" + target.getName() + "님을 Spectator 팀에 추가했습니다.");
                        }
                    } else {
                        sender.sendMessage("§c플레이어를 찾을 수 없습니다.");
                    }
                } else {
                    if (sender instanceof Player) {
                        if (main.spectatorTeam.hasEntry(sender.getName())) {
                            sender.sendMessage("§c당신은 이미 Spectator 팀입니다.");
                        } else {
                            main.spectatorTeam.addEntry(sender.getName());
                            ((Player) sender).setGameMode(GameMode.SPECTATOR);
                            for (PotionEffect effect : ((Player) sender).getActivePotionEffects()) {
                                ((Player) sender).removePotionEffect(effect.getType());
                            }
                            ((Player) sender).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            sender.sendMessage("§a당신을 Spectator 팀에 추가했습니다.");
                        }
                    }
                }
            }
            else {
                sender.sendMessage("§c권한이 없습니다.");
            }
        }
        else if (args[0].equals("sacrifice")) {
            if (sender.isOp()) {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (main.sacrificeTeam.hasEntry(target.getName())) {
                            sender.sendMessage("§c" + target.getName() + "은(는) 이미 Sacrifice 팀입니다.");
                        } else {
                            main.sacrificeTeam.addEntry(target.getName());
                            target.setGameMode(GameMode.SPECTATOR);
                            for (PotionEffect effect : target.getActivePotionEffects()) {
                                target.removePotionEffect(effect.getType());
                            }
                            target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            sender.sendMessage("§a" + target.getName() + "님을 Sacrifice 팀에 추가했습니다.");
                        }
                    } else {
                        sender.sendMessage("§c플레이어를 찾을 수 없습니다.");
                    }
                } else {
                    if (sender instanceof Player) {
                        if (main.sacrificeTeam.hasEntry(sender.getName())) {
                            sender.sendMessage("§c당신은 이미 Sacrifice 팀입니다.");
                        } else {
                            main.sacrificeTeam.addEntry(sender.getName());
                            ((Player) sender).setGameMode(GameMode.SPECTATOR);
                            for (PotionEffect effect : ((Player) sender).getActivePotionEffects()) {
                                ((Player) sender).removePotionEffect(effect.getType());
                            }
                            ((Player) sender).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                            sender.sendMessage("§a당신을 Sacrifice 팀에 추가했습니다.");
                        }
                    }
                }
            }
            else {
                sender.sendMessage("§c권한이 없습니다.");
            }
        }
        else if (args[0].equals("help")) {
            sender.sendMessage("§a/attach <type> [player]");
        }
        else {
            sender.sendMessage("§c사용법 : /attach <type> [player]");
        }
        return false;
    }
}
