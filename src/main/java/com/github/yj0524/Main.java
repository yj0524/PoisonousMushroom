package com.github.yj0524;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;

public class Main extends JavaPlugin implements Listener {

    ScoreboardManager scoreboardManager;
    Scoreboard scoreboard;

    private Team spectatorTeam;
    private Team mushroomTeam;
    private Team peopleTeam;

    // Config.yml 파일에 들어갈 값들
    int huskHealth;
    int huskCount;
    String mushroomPlayerName;
    boolean serverAutoShutDown;
    int serverShutDownTick;

    @Override
    public void onEnable() {
        getLogger().info("Plugin Enabled");

        // 레시피 불러오기
        loadRecipe();

        getCommand("poisonousmushroom").setTabCompleter(new TabCom());
        getCommand("util").setTabCompleter(new UtilTabCom());

        getServer().getPluginManager().registerEvents(this, this);

        addTeam();

        // Config.yml 파일 생성
        loadConfig();
        File cfile = new File(getDataFolder(), "config.yml");
        if (cfile.length() == 0) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled");
    }

    private void addTeam() {
        scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getMainScoreboard();

        Bukkit.getPluginManager().registerEvents(this, this);

        // 3개의 팀 생성
        if (scoreboard.getTeam("Spectator") == null) {
            spectatorTeam = scoreboard.registerNewTeam("Spectator");
            spectatorTeam.setDisplayName("Spectator");
            spectatorTeam.setPrefix(ChatColor.GRAY + "[Spectator] ");
            peopleTeam.setAllowFriendlyFire(false);
        } else {
            spectatorTeam = scoreboard.getTeam("Spectator");
        }

        if (scoreboard.getTeam("Mushroom") == null) {
            mushroomTeam = scoreboard.registerNewTeam("Mushroom");
            mushroomTeam.setDisplayName("Mushroom");
            mushroomTeam.setPrefix(ChatColor.RED + "[Mushroom] ");
            peopleTeam.setAllowFriendlyFire(false);
        } else {
            mushroomTeam = scoreboard.getTeam("Mushroom");
        }

        if (scoreboard.getTeam("People") == null) {
            peopleTeam = scoreboard.registerNewTeam("People");
            peopleTeam.setDisplayName("People");
            peopleTeam.setPrefix(ChatColor.AQUA + "[People] ");
            peopleTeam.setAllowFriendlyFire(false);
        } else {
            peopleTeam = scoreboard.getTeam("People");
        }
    }

    private void loadRecipe() {
        // 포자 퇴치기 레시피
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§c포자 퇴치기");
        item.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(this, "poison");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" T ", "AMC", " N ");
        recipe.setIngredient('T', Material.TRIDENT);
        recipe.setIngredient('A', Material.GOLDEN_APPLE);
        recipe.setIngredient('M', Material.GLISTERING_MELON_SLICE);
        recipe.setIngredient('C', Material.GOLDEN_CARROT);
        recipe.setIngredient('N', Material.NETHER_STAR);
        Bukkit.addRecipe(recipe);
    }

    private void loadConfig() {
        // Load chest size from config
        FileConfiguration config = getConfig();
        huskHealth = config.getInt("huskHealth", 20);
        huskCount = config.getInt("huskCount", 10);
        mushroomPlayerName = config.getString("mushroomPlayerName", "yj0524_kr");
        serverAutoShutDown = config.getBoolean("serverAutoShutDown", false);
        serverShutDownTick = config.getInt("serverShutDownTick", 600);
        // Save config
        config.set("huskHealth", huskHealth);
        config.set("huskCount", huskCount);
        config.set("mushroomPlayerName", mushroomPlayerName);
        config.set("serverAutoShutDown", serverAutoShutDown);
        config.set("serverShutDownTick", serverShutDownTick);
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // mushroomPlayerName이라는 닉네임이면 Mushroom 팀에 Join하고, 아니면 People 팀에 Join
        if (player.getName().equals(mushroomPlayerName)) {
            if (!mushroomTeam.hasEntry(player.getName())) {
                mushroomTeam.addEntry(player.getName());
            }
        } else {
            if (!peopleTeam.hasEntry(player.getName()) && !spectatorTeam.hasEntry(player.getName())) {
                peopleTeam.addEntry(player.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        World world = player.getWorld();
        Team playerTeam = scoreboard.getPlayerTeam(player);

        // People 팀에 있는 사람이 죽었다면
        if (playerTeam != null && playerTeam.getName().equals("People")) {
            String playerName = event.getEntity().getName();
            String message = "§c" + playerName + "님이 사망했습니다.";
            for (Player allplayers : Bukkit.getOnlinePlayers()) {
                allplayers.sendMessage(message);
            }

            Location deathLocation = player.getLocation();

            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);

            player.setGameMode(GameMode.SPECTATOR);
            spectatorTeam.addEntry(player.getName());

            // 죽은 위치에 허스크 1마리 생성
            Husk husk = (Husk) world.spawnEntity(deathLocation, EntityType.HUSK);
            husk.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(huskHealth);
            husk.setHealth(huskHealth);

            // 같은 팀 주변에 허스크 10마리 생성
            for (Player teamPlayer : Bukkit.getOnlinePlayers()) {
                if (teamPlayer != player && playerTeam.getName().equals("People")) {
                    Location playerLocation = teamPlayer.getLocation();
                    for (int i = 0; i < huskCount; i++) {
                        Location huskLocation = playerLocation.clone().add(Math.random() * 60 - 30, 0, Math.random() * 60 - 30);
                        Husk husks = (Husk) world.spawnEntity(huskLocation, EntityType.HUSK);
                        husks.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(huskHealth);
                        husks.setHealth(huskHealth);
                    }
                }
            }
        }

        // 죽은 플레이어가 Spectator 팀에 속한 경우
        if (scoreboard.getPlayerTeam(player).getName().equals("Spectator")) {
            // Spectator 팀에서 제거
            scoreboard.getTeam("Spectator").removeEntry(player.getName());

            // 죽은 플레이어를 Spectator로 설정하고 Spectator 팀에 추가
            player.setGameMode(GameMode.SPECTATOR);
            scoreboard.getTeam("Spectator").addEntry(player.getName());
        }

        // People 팀의 인원이 0명일 때
        if (peopleTeam.getEntries().size() == 0) {
            for (Player allplayers : Bukkit.getOnlinePlayers()) {
                if (serverAutoShutDown) {
                    // serverAutoShutDown이 true일 경우
                    allplayers.sendTitle("§c게임 종료", "§c모든 사람이 죽었습니다. §a" + (serverShutDownTick / 20) + "§a초 후에 서버가 종료됩니다.");
                    // Spectator 팀을 People 팀으로 모두 Join
                    for (String entry : spectatorTeam.getEntries()) {
                        Player player1 = Bukkit.getPlayer(entry);
                        if (player1 != null) {
                            // 만일 Spectator 모드에서 지면 위로 올라오지 않았거나 지면보다 높다면 제일 가까운 땅으로 이동
                            Location playerLocation = player1.getLocation();
                            Location groundLocation = playerLocation.clone().add(0, -1, 0);
                            if (playerLocation.getY() > groundLocation.getY()) {
                                player1.teleport(groundLocation);
                                player1.setGameMode(GameMode.SURVIVAL);
                                peopleTeam.addEntry(player1.getName());
                            }
                            player1.setGameMode(GameMode.SURVIVAL);
                            peopleTeam.addEntry(player1.getName());
                        }
                    }
                    // serverShutDownTick 틱 후에 서버 종료
                    Bukkit.getScheduler().runTaskLater(this, () -> Bukkit.getServer().shutdown(), serverShutDownTick);
                } else {
                    // serverAutoShutDown이 false일 경우
                    allplayers.sendTitle("§c게임 종료", "§c모든 사람이 죽었습니다.");
                    // Spectator 팀을 People 팀으로 모두 Join
                    for (String entry : spectatorTeam.getEntries()) {
                        Player player1 = Bukkit.getPlayer(entry);
                        if (player1 != null) {
                            // 만일 Spectator 모드에서 지면 위로 올라오지 않았거나 지면보다 높다면 제일 가까운 땅으로 이동
                            Location playerLocation = player1.getLocation();
                            Location groundLocation = playerLocation.clone().add(0, -1, 0);
                            if (playerLocation.getY() > groundLocation.getY()) {
                                player1.teleport(groundLocation);
                                player1.setGameMode(GameMode.SURVIVAL);
                                peopleTeam.addEntry(player1.getName());
                            }
                            player1.setGameMode(GameMode.SURVIVAL);
                            peopleTeam.addEntry(player1.getName());
                        }
                    }
                }
            }
        }
    }

    // 위 onEnable()에서 생성한 조합법으로 만든 아이템을 우클릭했을 때 게임 종료
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
            if (item.getItemMeta().getDisplayName().equals("§c포자 퇴치기")) {
                // 사용 후, 아이템 삭제
                player.getInventory().setItemInMainHand(null);

                // 게임 종료
                for (Player allplayers : Bukkit.getOnlinePlayers()) {
                    if (serverAutoShutDown) {
                        // serverAutoShutDown이 true일 경우
                        allplayers.sendTitle("§c게임 종료", "§a포자 퇴치기를 만들었습니다. §a" + (serverShutDownTick / 20) + "§a초 후에 서버가 종료됩니다.");
                        // Mushroom 팀을 Spectator 팀으로 모두 Join
                        for (String entry : mushroomTeam.getEntries()) {
                            Player player1 = Bukkit.getPlayer(entry);
                            if (player1 != null) {
                                player1.setGameMode(GameMode.SPECTATOR);
                                spectatorTeam.addEntry(player1.getName());
                            }
                        }
                        // "버섯이 소멸했습니다!" 라는 메시지를 모두에게 출력
                        for (Player allplayers1 : Bukkit.getOnlinePlayers()) {
                            allplayers1.sendMessage("§c버섯이 소멸했습니다!");
                        }
                        // 모든 플레이어에게 "버섯은 사라졌지만, 이미 인간도 감염되어있었다..." 라는 메시지를 게임이 종료되고 5초 후에 보냄
                        Bukkit.getScheduler().runTaskLater(this, () -> {
                            // People 팀을 Spectator 팀으로 모두 Join
                            for (String entry : peopleTeam.getEntries()) {
                                Player player1 = Bukkit.getPlayer(entry);
                                if (player1 != null) {
                                    player1.setGameMode(GameMode.SPECTATOR);
                                    spectatorTeam.addEntry(player1.getName());
                                }
                            }
                            for (Player allplayers1 : Bukkit.getOnlinePlayers()) {
                                allplayers1.sendMessage("§c버섯은 사라졌지만, 이미 인간도 감염되어있었다...");
                            }
                        }, 200);
                        // serverShutDownTick 틱 후에 서버 종료
                        Bukkit.getScheduler().runTaskLater(this, () -> Bukkit.getServer().shutdown(), serverShutDownTick + 200);
                    } else {
                        // serverAutoShutDown이 false일 경우
                        allplayers.sendTitle("§c게임 종료", "§a포자 퇴치기를 만들었습니다.");
                        // Mushroom 팀을 Spectator 팀으로 모두 Join
                        for (String entry : mushroomTeam.getEntries()) {
                            Player player1 = Bukkit.getPlayer(entry);
                            if (player1 != null) {
                                player1.setGameMode(GameMode.SPECTATOR);
                                spectatorTeam.addEntry(player1.getName());
                            }
                        }
                        // "버섯이 소멸했습니다!" 라는 메시지를 모두에게 출력
                        for (Player allplayers1 : Bukkit.getOnlinePlayers()) {
                            allplayers1.sendMessage("§c버섯이 소멸했습니다!");
                        }
                        // 모든 플레이어에게 "버섯은 사라졌지만, 이미 인간도 감염되어있었다..." 라는 메시지를 게임이 종료되고 5초 후에 보냄
                        Bukkit.getScheduler().runTaskLater(this, () -> {
                            // People 팀을 Spectator 팀으로 모두 Join
                            for (String entry : peopleTeam.getEntries()) {
                                Player player1 = Bukkit.getPlayer(entry);
                                if (player1 != null) {
                                    player1.setGameMode(GameMode.SPECTATOR);
                                    spectatorTeam.addEntry(player1.getName());
                                }
                            }
                            for (Player allplayers1 : Bukkit.getOnlinePlayers()) {
                                allplayers1.sendMessage("§c버섯은 사라졌지만, 이미 인간도 감염되어있었다...");
                            }
                        }, 200);
                    }
                }
            }
        }
    }

    // poisonousmushroom 명령어 입력 시
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("poisonousmushroom")) {
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
                            allplayers.sendTitle("§c게임 종료", "§a관리자가 게임을 종료했습니다.");
                        }
                        // Mushroom 팀을 Spectator 팀으로 모두 Join
                        for (String entry : mushroomTeam.getEntries()) {
                            Player player1 = Bukkit.getPlayer(entry);
                            if (player1 != null) {
                                player1.setGameMode(GameMode.SPECTATOR);
                                spectatorTeam.addEntry(player1.getName());
                            }
                        }
                        // People 팀을 Spectator 팀으로 모두 Join
                        for (String entry : peopleTeam.getEntries()) {
                            Player player1 = Bukkit.getPlayer(entry);
                            if (player1 != null) {
                                player1.setGameMode(GameMode.SPECTATOR);
                                spectatorTeam.addEntry(player1.getName());
                            }
                        }
                        if (serverAutoShutDown) {
                            // serverShutDownTick 틱 후에 서버 종료
                            Bukkit.getScheduler().runTaskLater(this, () -> Bukkit.getServer().shutdown(), serverShutDownTick + 200);
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
                                }
                                else if (player.getName().toString() != target.getName().toString()) {
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
        } else if (command.getName().equals("util")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.sendMessage("§c사용법 : /util <serverautoshutdown, servershutdowntick> [bool (serverautoshutdown command only), int (servershutdowntick command only)]");
                }
                else if (args[0].equals("serverautoshutdown")) {
                    if (player.isOp()) {
                        if (args.length == 1) {
                            player.sendMessage("§a서버 자동 종료 기능은" + serverAutoShutDown + "입니다.");
                        } else if (args.length == 2) {
                            if (args[1].equals("true")) {
                                serverAutoShutDown = true;
                                player.sendMessage("§a서버 자동 종료 기능을 활성화했습니다.");
                            } else if (args[1].equals("false")) {
                                serverAutoShutDown = false;
                                player.sendMessage("§a서버 자동 종료 기능을 비활성화했습니다.");
                            } else {
                                player.sendMessage("§c사용법 : /util serverautoshutdown [bool]");
                            }
                        }
                    } else {
                        player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                    }
                }
                else if (args[0].equals("servershutdowntick")) {
                    if (player.isOp()) {
                        if (args.length == 1) {
                            player.sendMessage("§a현재 서버 자동 종료 시간은 " + serverShutDownTick + "틱입니다.");
                        } else if (args.length == 2) {
                            try {
                                serverShutDownTick = Integer.parseInt(args[1]);
                                player.sendMessage("§a서버 자동 종료 시간을 " + serverShutDownTick + "틱으로 설정했습니다.");
                            } catch (NumberFormatException e) {
                                player.sendMessage("§c사용법 : /util servershutdowntick [int]");
                            }
                        }
                    } else {
                        player.sendMessage("§c당신은 이 명령어를 사용할 권한이 없습니다.");
                    }
                }
                else if (args[0].equals("help")) {
                    player.sendMessage("§a사용법 : /util <serverautoshutdown, servershutdowntick> [bool (serverautoshutdown command only), int (servershutdowntick command only)]");
                }
            }
        }
        return false;
    }
}