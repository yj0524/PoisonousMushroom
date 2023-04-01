package com.github.yj0524;

import com.github.yj0524.commands.*;
import com.github.yj0524.util.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
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

    public ScoreboardManager scoreboardManager;
    public Scoreboard scoreboard;

    public Team spectatorTeam;
    public Team mushroomTeam;
    public Team peopleTeam;

    public boolean isGameEnd = false;

    // Config.yml 파일에 들어갈 값들
    public int huskHealth;
    public int huskCount;
    public String mushroomPlayerName;
    public boolean serverAutoShutDown;
    public int serverShutDownTick;
    public int mobFollowRange;
    public int respawnSpectatorRange;
    public boolean mobSpawn;
    public double huskTridentPercent;

    @Override
    public void onEnable() {
        getLogger().info("Plugin Enabled");

        UpdateChecker.check(this, "yj0524", "PoisonousMushroom");

        // 레시피 불러오기
        loadRecipe();

        getCommand("poisonousmushroom").setExecutor(new PoisonousMushroom(this));
        getCommand("util").setExecutor(new Util(this));
        getCommand("update").setExecutor(new Update(this));
        getCommand("configreload").setExecutor(new ConfigReload(this));

        getCommand("poisonousmushroom").setTabCompleter(new TabCom());
        getCommand("util").setTabCompleter(new UtilTabCom());
        getCommand("update").setTabCompleter(new UpdateTabCom());
        getCommand("configreload").setTabCompleter(new ConfigReloadTabCom());

        addTeam();

        // Config.yml 파일 생성
        loadConfig();
        File cfile = new File(getDataFolder(), "config.yml");
        if (cfile.length() == 0) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        Bukkit.getPluginManager().registerEvents(this, this);

        gamerule("doMobSpawning", String.valueOf((Boolean) mobSpawn));
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled");

        isGameEnd = false;
    }

    public void gamerule(String gamerule, String value) {
        getServer().dispatchCommand(getServer().getConsoleSender(), "gamerule " + gamerule + " " + value);
    }

    private void addTeam() {
        scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getMainScoreboard();

        // 팀 3개 생성
        if (scoreboard.getTeam("Spectator") == null) {
            spectatorTeam = scoreboard.registerNewTeam("Spectator");
            spectatorTeam.setDisplayName("Spectator");
            spectatorTeam.setPrefix(ChatColor.GRAY + "[Spectator] ");
            spectatorTeam.setAllowFriendlyFire(false);
        } else {
            spectatorTeam = scoreboard.getTeam("Spectator");
        }

        if (scoreboard.getTeam("Mushroom") == null) {
            mushroomTeam = scoreboard.registerNewTeam("Mushroom");
            mushroomTeam.setDisplayName("Mushroom");
            mushroomTeam.setPrefix(ChatColor.RED + "[Mushroom] ");
            mushroomTeam.setAllowFriendlyFire(false);
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
        recipe.shape("T S", " N ", "AMC");
        recipe.setIngredient('T', Material.TRIDENT);
        recipe.setIngredient('S', Material.MUSHROOM_STEW);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.GOLDEN_APPLE);
        recipe.setIngredient('M', Material.GLISTERING_MELON_SLICE);
        recipe.setIngredient('C', Material.GOLDEN_CARROT);
        Bukkit.addRecipe(recipe);

        // 부활 신호기 레시피
        ItemStack item2 = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName("§a부활 신호기");
        item2.setItemMeta(meta2);
        NamespacedKey key2 = new NamespacedKey(this, "resurrection");
        ShapedRecipe recipe2 = new ShapedRecipe(key2, item2);
        recipe2.shape(" I ", " D ", " G ");
        recipe2.setIngredient('I', Material.IRON_INGOT);
        recipe2.setIngredient('D', Material.DIAMOND);
        recipe2.setIngredient('G', Material.GOLD_INGOT);
        Bukkit.addRecipe(recipe2);
    }

    public void loadConfig() {
        // Load config
        FileConfiguration config = getConfig();
        huskHealth = config.getInt("huskHealth", 20);
        huskCount = config.getInt("huskCount", 10);
        mushroomPlayerName = config.getString("mushroomPlayerName", "yj0524_kr");
        serverAutoShutDown = config.getBoolean("serverAutoShutDown", false);
        serverShutDownTick = config.getInt("serverShutDownTick", 600);
        mobFollowRange = config.getInt("mobFollowRange", 128);
        respawnSpectatorRange = config.getInt("respawnSpectatorRange", 10);
        mobSpawn = config.getBoolean("mobSpawn", true);
        huskTridentPercent = config.getDouble("huskTridentPercent", 10.0);
        // Save config
        config.set("huskHealth", huskHealth);
        config.set("huskCount", huskCount);
        config.set("mushroomPlayerName", mushroomPlayerName);
        config.set("serverAutoShutDown", serverAutoShutDown);
        config.set("serverShutDownTick", serverShutDownTick);
        config.set("mobFollowRange", mobFollowRange);
        config.set("respawnSpectatorRange", respawnSpectatorRange);
        config.set("mobSpawn", mobSpawn);
        config.set("huskTridentPercent", huskTridentPercent);
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // mushroomPlayerName이라는 닉네임이면 Mushroom 팀에 Join하고, 아니면 People 팀에 Join
        if (player.getName().equals(mushroomPlayerName)) {
            if (!mushroomTeam.hasEntry(player.getName())) {
                mushroomTeam.addEntry(player.getName());
                player.setGameMode(GameMode.SURVIVAL);
            }
        } else {
            if (!peopleTeam.hasEntry(player.getName()) && !spectatorTeam.hasEntry(player.getName())) {
                peopleTeam.addEntry(player.getName());
            }
            else if (isGameEnd) {
                if (peopleTeam.hasEntry(player.getName())) {
                    player.setGameMode(GameMode.SURVIVAL);
                }
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

            // 같은 팀 주변에 허스크 생성
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
                    allplayers.sendTitle("§c게임 종료", "§c모든 사람이 죽었습니다. " + (serverShutDownTick / 20) + "초 후에 서버가 종료됩니다.");
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
                if (!mushroomTeam.hasEntry(player.getName())) {
                    // 사용 후, 아이템 삭제
                    player.getInventory().setItemInMainHand(null);

                    // 게임 종료
                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        if (serverAutoShutDown) {
                            // serverAutoShutDown이 true일 경우
                            allplayers.sendTitle("§c게임 종료", "§a포자 퇴치기를 만들었습니다." + (serverShutDownTick / 20) + " 초 후에 서버가 종료됩니다.");
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
                else {
                    player.sendMessage("§c사용할 수 없습니다!");
                }
            }
        }

        else if (item != null && item.getType() == Material.HEART_OF_THE_SEA) {
            if (item.getItemMeta().getDisplayName().equals("§a부활 신호기")) {
                if (!mushroomTeam.hasEntry(player.getName())) {
                    // 사용 후, 아이템 삭제
                    player.getInventory().setItemInMainHand(null);

                    int tmpPlayer = peopleTeam.getSize();

                    // 사용한 사람 기준 주변 10블록 관전자에게 People 팀으로 Join
                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        if (allplayers.getGameMode() == GameMode.SPECTATOR && spectatorTeam.hasEntry(allplayers.getName())) {
                            if (allplayers.getLocation().distance(player.getLocation()) <= respawnSpectatorRange) {
                                allplayers.setGameMode(GameMode.SURVIVAL);
                                spectatorTeam.removeEntry(allplayers.getName());
                                peopleTeam.addEntry(allplayers.getName());
                            }
                        }
                    }

                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        allplayers.sendMessage("§a" + player.getName() + " 님이 부활 신호기를 사용했습니다!");

                        if (tmpPlayer == peopleTeam.getSize()) {
                            allplayers.sendMessage("§c하지만, 주변에 영혼이 없어서 부활에 실패했습니다!");
                            allplayers.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                        } else allplayers.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                }
                else {
                    player.sendMessage("§c사용할 수 없습니다!");
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (mobSpawn) {
            if (event.getEntityType() == EntityType.ZOMBIE || event.getEntityType() == EntityType.HUSK || event.getEntityType() == EntityType.DROWNED) {
                if (event.getEntityType() == EntityType.ZOMBIE) {
                    Zombie zombie = (Zombie) event.getEntity();
                    zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(mobFollowRange);
                    zombie.getEquipment().setHelmet(new ItemStack(Material.STONE_BUTTON));
                }

                if (event.getEntityType() == EntityType.HUSK) {
                    Husk husk = (Husk) event.getEntity();
                    husk.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(mobFollowRange);
                }

                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                    if (allPlayers.getGameMode() == GameMode.SURVIVAL || allPlayers.getGameMode() == GameMode.ADVENTURE && allPlayers.getLocation().distance(event.getLocation()) <= mobFollowRange) {
                        if (event.getEntityType() == EntityType.ZOMBIE) {
                            Zombie zombie = (Zombie) event.getEntity();
                            zombie.setTarget(allPlayers);
                        }

                        if (event.getEntityType() == EntityType.HUSK) {
                            Husk husk = (Husk) event.getEntity();
                            husk.setTarget(allPlayers);
                        }
                    }
                }

                if (event.getEntityType() == EntityType.HUSK) {
                    Husk husk = (Husk) event.getEntity();
                    if (!husk.isAdult()) {
                        husk.getWorld().spawnEntity(husk.getLocation(), EntityType.HUSK);
                        husk.remove();
                    }
                }

                if (event.getEntityType() == EntityType.DROWNED) {
                    Drowned drowned = (Drowned) event.getEntity();
                    drowned.getWorld().spawnEntity(drowned.getLocation(), EntityType.HUSK);
                    drowned.remove();
                    if (!drowned.isAdult()) {
                        drowned.getWorld().spawnEntity(drowned.getLocation(), EntityType.HUSK);
                        drowned.remove();
                    }
                }
            }
        }
        else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (mushroomTeam.hasEntry(player.getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.HUSK) {
            Husk husk = (Husk) event.getEntity();
            if (husk.getKiller() != null) {
                if (Math.random() <= huskTridentPercent / 100) {
                    husk.getWorld().dropItemNaturally(husk.getLocation(), new ItemStack(Material.TRIDENT));
                }
            }
        }
    }
}
