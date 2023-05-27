package com.github.yj0524.commands;

import com.github.yj0524.Main;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class Util implements CommandExecutor {

    Main main;

    World overworld = getServer().getWorld("world");
    World nether = getServer().getWorld("world_nether");
    World end = getServer().getWorld("world_the_end");

    public Util(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("§c사용법 : /util <type> [value]");
            } else if (args[0].equals("huskhealth")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 허스크 체력은 " + main.huskHealth + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.huskHealth = Integer.parseInt(args[1]);
                            main.configSave();
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
                            main.configSave();
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
                        main.configSave();
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
                            main.configSave();
                            player.sendMessage("§a서버 자동 종료 기능을 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.serverAutoShutDown = false;
                            main.configSave();
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
                            main.configSave();
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
                            main.configSave();
                            player.sendMessage("§a몹 팔로우 범위를 " + main.mobFollowRange + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util mobfollowrange [int]");
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
                            main.configSave();
                            player.sendMessage("§a리스폰 스펙토어 범위를 " + main.respawnSpectatorRange + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util respawnspectatorrange [int]");
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
                            main.configSave();
                            overworld.setGameRule(GameRule.DO_MOB_SPAWNING, main.mobSpawn);
                            nether.setGameRule(GameRule.DO_MOB_SPAWNING, main.mobSpawn);
                            end.setGameRule(GameRule.DO_MOB_SPAWNING, main.mobSpawn);
                            player.sendMessage("§a몹 스폰 기능을 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.mobSpawn = false;
                            main.configSave();
                            overworld.setGameRule(GameRule.DO_MOB_SPAWNING, main.mobSpawn);
                            nether.setGameRule(GameRule.DO_MOB_SPAWNING, main.mobSpawn);
                            end.setGameRule(GameRule.DO_MOB_SPAWNING, main.mobSpawn);
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
                            main.huskTridentPercent = Float.parseFloat(args[1]);
                            main.configSave();
                            player.sendMessage("§a허스크가 삼지창을 떨어뜨릴 확률을 " + main.huskTridentPercent + "%로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util husktridentpercent [float]");
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
                            main.configSave();
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
                            main.configSave();
                            player.sendMessage("§a월드 보더 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.worldBorderEnable = false;
                            main.configSave();
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
                            main.configSave();
                            player.sendMessage("§a엔드 게이트웨이 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.endGateway = false;
                            main.configSave();
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
                            main.configSave();
                            player.sendMessage("§a랜덤 스폰 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.randomSpawn = false;
                            main.configSave();
                            player.sendMessage("§a랜덤 스폰 활성화 여부를 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util randomspawn [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("peoplehealth")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 생존자의 체력은 " + main.peopleHealth + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.peopleHealth = Float.parseFloat(args[1]);
                            main.configSave();
                            player.sendMessage("§a생존자의 체력을 " + main.peopleHealth + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util peopleHealth [float]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("mushroomhealth")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 버섯의 체력은 " + main.mushroomHealth + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.mushroomHealth = Float.parseFloat(args[1]);
                            main.configSave();
                            player.sendMessage("§a버섯의 체력을 " + main.mushroomHealth + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util mushroomHealth [float]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("supermushroomhealth")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 슈퍼 버섯의 체력은 " + main.superMushroomHealth + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.superMushroomHealth = Float.parseFloat(args[1]);
                            main.configSave();
                            player.sendMessage("§a슈퍼 버섯의 체력을 " + main.superMushroomHealth + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util supermushroomHealth [float]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("sacrificepercent")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 희생 성공 확률은 " + main.sacrificePercent + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.sacrificePercent = Float.parseFloat(args[1]);
                            main.configSave();
                            player.sendMessage("§a희생 성공 확률을 " + main.sacrificePercent + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util sacrificepercent [float]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("infectionpercent")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 감염 성공 확률은 " + main.infectionPercent + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.infectionPercent = Float.parseFloat(args[1]);
                            main.configSave();
                            player.sendMessage("§a감염 성공 확률을 " + main.infectionPercent + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util infectionpercent [float]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("infectionenable")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 감염 활성화 여부는 " + main.infectionEnable + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.infectionEnable = true;
                            main.configSave();
                            player.sendMessage("§a감염 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.infectionEnable = false;
                            main.configSave();
                            player.sendMessage("§a감염 활성화 여부를 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util infectionenable [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("gameendmessageenable")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 게임 종료 메시지 활성화 여부는 " + main.gameEndMessageEnable + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.gameEndMessageEnable = true;
                            main.configSave();
                            player.sendMessage("§a게임 종료 메시지 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.gameEndMessageEnable = false;
                            main.configSave();
                            player.sendMessage("§a게임 종료 메시지 활성화 여부를 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util gameendmessageenable [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("informationenable")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 정보 활성화 여부는 " + main.informationEnable + "입니다.");
                    } else if (args.length == 2) {
                        if (args[1].equals("true")) {
                            main.informationEnable = true;
                            main.configSave();
                            player.sendMessage("§a정보 활성화 여부를 활성화했습니다.");
                        } else if (args[1].equals("false")) {
                            main.informationEnable = false;
                            main.configSave();
                            player.sendMessage("§a정보 활성화 여부를 비활성화했습니다.");
                        } else {
                            player.sendMessage("§c사용법 : /util informationenable [bool]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("foodlevel")) {
                if (player.isOp()) {
                    if (args.length == 1) {
                        player.sendMessage("§a현재 허기 수치는 " + main.foodLevel + "입니다.");
                    } else if (args.length == 2) {
                        try {
                            main.foodLevel = Float.parseFloat(args[1]);
                            main.configSave();
                            player.sendMessage("§a허기 수치를 " + main.foodLevel + "으로 설정했습니다.");
                        } catch (NumberFormatException e) {
                            player.sendMessage("§c사용법 : /util foodlevel [float]");
                        }
                    }
                } else {
                    player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                }
            } else if (args[0].equals("help")) {
                player.sendMessage("§a사용법 : /util <type> [value]");
            }
        }
        return true;
    }
}
