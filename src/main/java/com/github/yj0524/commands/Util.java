package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util implements CommandExecutor {

    Main main;

    public Util(Main main) {
        this.main = main;
    }

    public void saveConfig() {
        main.getConfig().set("huskHealth", main.huskHealth);
        main.getConfig().set("huskCount", main.huskCount);
        main.getConfig().set("mushroomPlayerName", main.mushroomPlayerName);
        main.getConfig().set("serverAutoShutDown", main.serverAutoShutDown);
        main.getConfig().set("serverShutDownTick", main.serverShutDownTick);
        main.getConfig().set("mobFollowRange", main.mobFollowRange);
        main.getConfig().set("respawnSpectatorRange", main.respawnSpectatorRange);
        main.getConfig().set("mobSpawn", main.mobSpawn);
        main.getConfig().set("huskTridentPercent", main.huskTridentPercent);
        main.getConfig().set("worldBorderSize", main.worldBorderSize);
        main.getConfig().set("worldBorderEnable", main.worldBorderEnable);
        main.getConfig().set("endGateway", main.endGateway);
        main.saveConfig();
    }

    public void gamerule(String gamerule, String value) {
        main.getServer().dispatchCommand(main.getServer().getConsoleSender(), "gamerule " + gamerule + " " + value);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("§c사용법 : /util <huskhealth, huskcount, mushroomplayername, serverautoshutdown, servershutdowntick, mobfollowrange, respawnspectatorrange, mobspawn, husktridentpercent, worldbordersize, worldborderenable, endgateway, randomspawn>");
            } else if (args[0].equals("huskhealth")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 허스크 체력은 " + main.huskHealth + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.huskHealth = Integer.parseInt(args[1]);
                            saveConfig();
                            player.sendMessage("§a허스크 체력을 " + main.huskHealth + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util huskhealth [int]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("huskcount")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 허스크 스폰 개수는 " + main.huskCount + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.huskCount = Integer.parseInt(args[1]);
                            saveConfig();
                            player.sendMessage("§a허스크 스폰 개수를 " + main.huskCount + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util huskcount [int]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("mushroomplayername")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 버섯 플레이어 이름은 " + main.mushroomPlayerName + "입니다.");
                    } else if (args.length == 2) {
                        main.mushroomPlayerName = args[1];
                        saveConfig();
                        player.sendMessage("§a버섯 플레이어 이름을 " + main.mushroomPlayerName + "으로 설정했습니다.");
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("serverautoshutdown")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 서버 자동 종료 기능은 " + main.serverAutoShutDown + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.serverAutoShutDown = true;
                            saveConfig();
                            player.sendMessage("§a서버 자동 종료 기능을 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.serverAutoShutDown = false;
                            saveConfig();
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
                            saveConfig();
                            player.sendMessage("§a서버 자동 종료 시간을 " + main.serverShutDownTick + "틱으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util servershutdowntick [int]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("mobfollowrange")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 몹 팔로우 범위는 " + main.mobFollowRange + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.mobFollowRange = Integer.parseInt(args[1]);
                            saveConfig();
                            player.sendMessage("§a몹 팔로우 범위를 " + main.mobFollowRange + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util mobfollowrange [double]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("respawnspectatorrange")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 리스폰 스펙토어 범위는 " + main.respawnSpectatorRange + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.respawnSpectatorRange = Integer.parseInt(args[1]);
                            saveConfig();
                            player.sendMessage("§a리스폰 스펙토어 범위를 " + main.respawnSpectatorRange + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util respawnspectatorrange [double]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("mobspawn")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 몹 스폰 기능은 " + main.mobSpawn + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.mobSpawn = true;
                            saveConfig();
                            gamerule("doMobSpawning", String.valueOf((Boolean) main.mobSpawn));
                            player.sendMessage("§a몹 스폰 기능을 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.mobSpawn = false;
                            saveConfig();
                            gamerule("doMobSpawning", String.valueOf((Boolean) main.mobSpawn));
                            player.sendMessage("§a몹 스폰 기능을 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util mobspawn [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("husktridentpercent")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 허스크가 삼지창을 떨어뜨릴 확률은 " + main.huskTridentPercent + "%입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.huskTridentPercent = Double.parseDouble(args[1]);
                            saveConfig();
                            player.sendMessage("§a허스크가 삼지창을 떨어뜨릴 확률을 " + main.huskTridentPercent + "%로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util husktridentpercent [double]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("worldbordersize")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 월드 보더 크기는 " + main.worldBorderSize + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.worldBorderSize = Integer.parseInt(args[1]);
                            saveConfig();
                            player.sendMessage("§a월드 보더 크기를 " + main.worldBorderSize + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util worldbordersize [int]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("worldborderenable")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 월드 보더 활성화 여부는 " + main.worldBorderEnable + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.worldBorderEnable = true;
                            saveConfig();
                            gamerule("doImmediateRespawn", String.valueOf((Boolean) main.worldBorderEnable));
                            player.sendMessage("§a월드 보더 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.worldBorderEnable = false;
                            saveConfig();
                            gamerule("doImmediateRespawn", String.valueOf((Boolean) main.worldBorderEnable));
                            player.sendMessage("§a월드 보더 활성화 여부를 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util worldborderenable [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("endgateway")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 엔드 게이트웨이 활성화 여부는 " + main.endGateway + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.endGateway = true;
                            saveConfig();
                            gamerule("doImmediateRespawn", String.valueOf((Boolean) main.endGateway));
                            player.sendMessage("§a엔드 게이트웨이 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.endGateway = false;
                            saveConfig();
                            gamerule("doImmediateRespawn", String.valueOf((Boolean) main.endGateway));
                            player.sendMessage("§a엔드 게이트웨이 활성화 여부를 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util endgateway [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("randomspawn")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 랜덤 스폰 활성화 여부는 " + main.randomSpawn + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.randomSpawn = true;
                            saveConfig();
                            gamerule("doImmediateRespawn", String.valueOf((Boolean) main.randomSpawn));
                            player.sendMessage("§a랜덤 스폰 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.randomSpawn = false;
                            saveConfig();
                            gamerule("doImmediateRespawn", String.valueOf((Boolean) main.randomSpawn));
                            player.sendMessage("§a랜덤 스폰 활성화 여부를 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util randomspawn [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("help")) {
                player.sendMessage("§a사용법 : /util <huskhealth, huskcount, mushroomplayername, serverautoshutdown, servershutdowntick, mobfollowrange, respawnspectatorrange, mobspawn, husktridentpercent, worldbordersize, worldborderenable, endgateway, randomspawn>");
            }
        }
        return true;
    }
}
