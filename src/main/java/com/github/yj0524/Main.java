package com.github.yj0524;

import com.github.yj0524.commands.*;
import com.github.yj0524.util.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.File;

public class Main extends JavaPlugin implements Listener {

    public ScoreboardManager scoreboardManager;
    public Scoreboard scoreboard;

    public Team spectatorTeam;
    public Team sacrificeTeam;
    public Team mushroomTeam;
    public Team superMushroomTeam;
    public Team peopleTeam;

    public boolean isGameEnd = false;
    public final double[] damageMultipliers = { 1.2, 1.4, 1.6, 1.8, 2.0 };
    public String version = getDescription().getVersion();

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
    public int worldBorderSize;
    public boolean worldBorderEnable;
    public boolean endGateway;
    public boolean randomSpawn;
    public double peopleHealth;
    public double mushroomHealth;
    public double superMushroomHealth;
    public double sacrificePercent;
    public double infectionPercent;
    public boolean infectionEnable;

    @Override
    public void onEnable() {
        getLogger().info("Plugin Enabled");

        UpdateChecker.check(this, "yj0524", "PoisonousMushroom");

        getCommand("poisonousmushroom").setExecutor(new PoisonousMushroom(this));
        getCommand("util").setExecutor(new Util(this));
        getCommand("update").setExecutor(new Update(this));
        getCommand("configreload").setExecutor(new ConfigReload(this));
        getCommand("evolve").setExecutor(new Evolve(this));
        getCommand("attach").setExecutor(new Attach(this));
        getCommand("sacrifice").setExecutor(new Sacrifice(this));

        getCommand("poisonousmushroom").setTabCompleter(new TabCom());
        getCommand("util").setTabCompleter(new UtilTabCom());
        getCommand("update").setTabCompleter(new UpdateTabCom());
        getCommand("configreload").setTabCompleter(new ConfigReloadTabCom());
        getCommand("evolve").setTabCompleter(new EvolveTabCom());
        getCommand("attach").setTabCompleter(new AttachTabCom());
        getCommand("sacrifice").setTabCompleter(new SacrificeTabCom());

        // Config.yml 파일 생성
        loadConfig();
        File cfile = new File(getDataFolder(), "config.yml");
        if (cfile.length() == 0) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        addTeam();
        loadRecipe();
        loadScoreboard();
        setWorldBorder();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (OfflinePlayer player : mushroomTeam.getPlayers()) {
                    if (player.isOnline()) {
                        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, -1, 0, false, false));
                        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 0, false, false));
                        player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mushroomHealth);
                    }
                }
                for (OfflinePlayer player : superMushroomTeam.getPlayers()) {
                    if (player.isOnline()) {
                        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, -1, 0, false, false));
                        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, -1, 0, false, false));
                        player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(superMushroomHealth);
                    }
                }
                for (OfflinePlayer player : peopleTeam.getPlayers()) {
                    if (player.isOnline()) {
                        player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(peopleHealth);
                    }
                }
                for (OfflinePlayer player : spectatorTeam.getPlayers()) {
                    if (player.isOnline()) {
                        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));
                    }
                }
                for (OfflinePlayer player : sacrificeTeam.getPlayers()) {
                    if (player.isOnline()) {
                        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));
                    }
                }
            }
        }.runTaskTimer(this, 0, 1);

        Bukkit.getPluginManager().registerEvents(this, this);

        gamerule("doMobSpawning", String.valueOf((Boolean) mobSpawn));
        gamerule("fallDamage", String.valueOf(false));
        gamerule("doImmediateRespawn", String.valueOf(true));
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

        // 팀 생성
        if (scoreboard.getTeam("Spectator") == null) {
            spectatorTeam = scoreboard.registerNewTeam("Spectator");
            spectatorTeam.setDisplayName("Spectator");
            spectatorTeam.setPrefix(ChatColor.GRAY + "[Spectator] ");
            spectatorTeam.setAllowFriendlyFire(false);
        } else {
            spectatorTeam = scoreboard.getTeam("Spectator");
        }

        if (scoreboard.getTeam("Sacrifice") == null) {
            sacrificeTeam = scoreboard.registerNewTeam("Sacrifice");
            sacrificeTeam.setDisplayName("Sacrifice");
            sacrificeTeam.setPrefix(ChatColor.RED + "[Sacrifice] ");
            sacrificeTeam.setAllowFriendlyFire(false);
        } else {
            sacrificeTeam = scoreboard.getTeam("Sacrifice");
        }

        if (scoreboard.getTeam("Mushroom") == null) {
            mushroomTeam = scoreboard.registerNewTeam("Mushroom");
            mushroomTeam.setDisplayName("Mushroom");
            mushroomTeam.setPrefix(ChatColor.RED + "[Mushroom] ");
            mushroomTeam.setAllowFriendlyFire(false);
        } else {
            mushroomTeam = scoreboard.getTeam("Mushroom");
        }

        if (scoreboard.getTeam("SuperMushroom") == null) {
            superMushroomTeam = scoreboard.registerNewTeam("SuperMushroom");
            superMushroomTeam.setDisplayName("SuperMushroom");
            superMushroomTeam.setPrefix(ChatColor.RED + "[SuperMushroom] ");
            superMushroomTeam.setAllowFriendlyFire(false);
        } else {
            superMushroomTeam = scoreboard.getTeam("SuperMushroom");
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
        // 부활 신호기 레시피
        ItemStack respawnsemaphore = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta respawnsemaphoreMeta = respawnsemaphore.getItemMeta();
        respawnsemaphoreMeta.setDisplayName("§a부활 신호기");
        respawnsemaphore.setItemMeta(respawnsemaphoreMeta);
        NamespacedKey key2 = new NamespacedKey(this, "respawnsemaphore");
        ShapedRecipe respawnsemaphoreRecipe = new ShapedRecipe(key2, respawnsemaphore);
        respawnsemaphoreRecipe.shape(" I ", " D ", " G ");
        respawnsemaphoreRecipe.setIngredient('I', Material.IRON_INGOT);
        respawnsemaphoreRecipe.setIngredient('D', Material.DIAMOND);
        respawnsemaphoreRecipe.setIngredient('G', Material.GOLD_INGOT);
        Bukkit.addRecipe(respawnsemaphoreRecipe);

        // 포자 퇴치기 레시피
        ItemStack vaccine = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta vaccineMeta = vaccine.getItemMeta();
        vaccineMeta.setDisplayName("§c포자 퇴치기");
        vaccine.setItemMeta(vaccineMeta);
        NamespacedKey key = new NamespacedKey(this, "vaccine");
        ShapedRecipe vaccineRecipe = new ShapedRecipe(key, vaccine);
        vaccineRecipe.shape("T S", " N ", "EAC");
        vaccineRecipe.setIngredient('T', Material.TRIDENT);
        vaccineRecipe.setIngredient('S', Material.MUSHROOM_STEW);
        vaccineRecipe.setIngredient('N', new ItemStack(respawnsemaphore));
        vaccineRecipe.setIngredient('E', Material.EMERALD);
        vaccineRecipe.setIngredient('A', Material.GOLDEN_APPLE);
        vaccineRecipe.setIngredient('C', Material.COPPER_INGOT);
        Bukkit.addRecipe(vaccineRecipe);
    }

    private void setWorldBorder() {
        if (worldBorderEnable) {
            WorldBorder worldBorder_over = getServer().getWorld("world").getWorldBorder();
            worldBorder_over.setCenter(0, 0);
            worldBorder_over.setSize(worldBorderSize);

            WorldBorder worldBorder_nether = getServer().getWorld("world_nether").getWorldBorder();
            worldBorder_nether.setCenter(0, 0);
            worldBorder_nether.setSize(worldBorderSize);

            if (!endGateway) {
                WorldBorder worldBorder_end = getServer().getWorld("world_the_end").getWorldBorder();
                worldBorder_end.setCenter(0, 0);
                worldBorder_end.setSize(512);
            }
        }
    }

    private void loadScoreboard() {
        Objective objective;

        if (scoreboard.getObjective("Information") != null) {
            scoreboard.getObjective("Information").unregister();
        }

        objective = scoreboard.registerNewObjective("Information", Criteria.DUMMY, ChatColor.AQUA + "Information");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(ChatColor.RED + "" + ChatColor.BOLD + "POISONOUS MUSHROOM").setScore(5);
        objective.getScore(ChatColor.RED + "" + ChatColor.BOLD + "포자 : 최후의 생존자들").setScore(4);
        objective.getScore(ChatColor.WHITE + " ").setScore(3);
        objective.getScore(ChatColor.GREEN + "Version " + version).setScore(2);
        objective.getScore(ChatColor.GREEN + "Minecraft 1.19.4").setScore(1);
        objective.getScore(ChatColor.GREEN + "Made by yj0524_kr").setScore(0);
        for (String entry : scoreboard.getEntries()) {
            if (scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(entry).getScore() < 0) {
                scoreboard.resetScores(entry);
            }
        }
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
        worldBorderSize = config.getInt("worldBorderSize", 2048);
        worldBorderEnable = config.getBoolean("worldBorderEnable", true);
        endGateway = config.getBoolean("endGateway", false);
        randomSpawn = config.getBoolean("randomSpawn", true);
        peopleHealth = config.getDouble("peopleHealth", 20.0);
        mushroomHealth = config.getDouble("mushroomHealth", 20.0);
        superMushroomHealth = config.getDouble("superMushroomHealth", 40.0);
        sacrificePercent = config.getDouble("sacrificePercent", 30.0);
        infectionPercent = config.getDouble("infectionPercent", 15.0);
        infectionEnable = config.getBoolean("infectionEnable", true);
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
        config.set("worldBorderSize", worldBorderSize);
        config.set("worldBorderEnable", worldBorderEnable);
        config.set("endGateway", endGateway);
        config.set("randomSpawn", randomSpawn);
        config.set("peopleHealth", peopleHealth);
        config.set("mushroomHealth", mushroomHealth);
        config.set("superMushroomHealth", superMushroomHealth);
        config.set("sacrificePercent", sacrificePercent);
        config.set("infectionPercent", infectionPercent);
        config.set("infectionEnable", infectionEnable);
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // mushroomPlayerName이라는 닉네임이면 Mushroom 팀에 Join하고, 아니면 People 팀에 Join
        if (player.getName().equals(mushroomPlayerName) && !superMushroomTeam.hasEntry(player.getName())) {
            if (!mushroomTeam.hasEntry(player.getName())) {
                mushroomTeam.addEntry(player.getName());
                player.setGameMode(GameMode.SURVIVAL);
            }
        } else {
            if (!peopleTeam.hasEntry(player.getName()) && !spectatorTeam.hasEntry(player.getName()) && !sacrificeTeam.hasEntry(player.getName())) {
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

        // Mushroom 팀에 있는 사람이 죽었다면
        if (playerTeam != null) {
            if (playerTeam.getName().equals("Mushroom") || playerTeam.getName().equals("SuperMushroom")) {
                String message = "§a버섯이 죽었습니다! 버섯이 월드의 스폰으로 이동했습니다!";
                for (Player allplayers : Bukkit.getOnlinePlayers()) {
                    allplayers.sendMessage(message);
                }
            }
        }

        if (playerTeam != null) {
            if (playerTeam.getName().equals("Mushroom") || playerTeam.getName().equals("SuperMushroom")) {
                return;
            }
        }

        if (infectionEnable) {
            if (Math.random() < infectionPercent / 100) {
                String message = "§c" + player.getName() + "님이 포자 바이러스에 감염되어 버섯으로 변했습니다! 부활 신호기의 영향을 받지 못합니다!";
                for (Player allplayers : Bukkit.getOnlinePlayers()) {
                    allplayers.sendMessage(message);
                }
                player.setGameMode(GameMode.SURVIVAL);
                mushroomTeam.addEntry(player.getName());
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                return;
            }
        }

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
                if (scoreboard.getPlayerTeam(teamPlayer).getName().equals("People")) {
                    Location playerLocation = teamPlayer.getLocation();
                    for (int i = 0; i < huskCount; i++) {
                        Location huskLocation = playerLocation.clone().add(Math.random() * 60 - 30, 0, Math.random() * 60 - 30);
                        huskLocation.setY(world.getHighestBlockYAt(huskLocation));
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
                            playerLocation.setY(world.getHighestBlockYAt(playerLocation));
                            player1.setGameMode(GameMode.SURVIVAL);
                            player1.teleport(playerLocation);
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
                            playerLocation.setY(world.getHighestBlockYAt(playerLocation));
                            player1.setGameMode(GameMode.SURVIVAL);
                            player1.teleport(playerLocation);
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
                if (peopleTeam.hasEntry(player.getName())) {
                    // 사용 후, 아이템 1개 삭제
                    item.setAmount(item.getAmount() - 1);

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
                if (peopleTeam.hasEntry(player.getName())) {
                    // 사용 후, 아이템 1개 삭제
                    item.setAmount(item.getAmount() - 1);

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
                            allplayers.playSound(allplayers.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                        } else {
                            String revivedPlayers = "";
                            for (String revivedPlayer : peopleTeam.getEntries()) {
                                revivedPlayers += revivedPlayer + ", ";
                            }
                            revivedPlayers = revivedPlayers.substring(0, revivedPlayers.length() - 2);
                            allplayers.sendMessage("§a" + revivedPlayers + " 님이 부활했습니다!");
                            allplayers.playSound(allplayers.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        }
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
                        if (mushroomTeam.hasEntry(allPlayers.getName()) || superMushroomTeam.hasEntry(allPlayers.getName())) {
                            return;
                        }
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

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getEntity() instanceof Monster && event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (mushroomTeam.hasEntry(player.getName()) || superMushroomTeam.hasEntry(player.getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();

        if (weapon.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
            int enchantmentLevel = weapon.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
            double damageMultiplier = damageMultipliers[enchantmentLevel - 1];

            if (event.getEntity() instanceof Player) {
                Player target = (Player) event.getEntity();
                if (mushroomTeam.hasEntry(target.getName()) || superMushroomTeam.hasEntry(target.getName())) {
                    event.setDamage(event.getDamage() * damageMultiplier);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        World world = player.getWorld();

        for (int x = playerLocation.getBlockX() - 1; x <= playerLocation.getBlockX() + 1; x++) {
            for (int y = playerLocation.getBlockY() - 1; y <= playerLocation.getBlockY() + 1; y++) {
                for (int z = playerLocation.getBlockZ() - 1; z <= playerLocation.getBlockZ() + 1; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.END_GATEWAY) {
                        player.teleport(new Location(world, 0, 75, 0));
                        player.sendMessage("§c이 서버에서는 엔드 게이트웨이를 사용할 수 없습니다!");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, -1, 0));
                        Bukkit.getScheduler().runTaskLater(this, () -> {
                            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                        }, 100);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (mushroomTeam.hasEntry(event.getPlayer().getName()) || superMushroomTeam.hasEntry(event.getPlayer().getName())) {
            Player player = event.getPlayer();
            Location location = player.getLocation();
            World world = player.getWorld();

            int x = (int) (Math.random() * worldBorderSize / 2);
            int z = (int) (Math.random() * worldBorderSize / 2);
            int y = world.getHighestBlockYAt(x, z);
            event.setRespawnLocation(new Location(world, x, y, z));
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (mushroomTeam.hasEntry(event.getWhoClicked().getName())) {
            if (event.getRecipe().getResult().getType() == Material.DIAMOND_SWORD || event.getRecipe().getResult().getType() == Material.DIAMOND_PICKAXE || event.getRecipe().getResult().getType() == Material.DIAMOND_AXE || event.getRecipe().getResult().getType() == Material.DIAMOND_SHOVEL || event.getRecipe().getResult().getType() == Material.DIAMOND_HOE || event.getRecipe().getResult().getType() == Material.DIAMOND_HELMET || event.getRecipe().getResult().getType() == Material.DIAMOND_CHESTPLATE || event.getRecipe().getResult().getType() == Material.DIAMOND_LEGGINGS || event.getRecipe().getResult().getType() == Material.DIAMOND_BOOTS) {
                event.getWhoClicked().sendMessage("§c버섯은 다이아몬드 도구, 갑옷 제작이 불가능합니다!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByMushroom(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (mushroomTeam.hasEntry(player.getName())) {
                if (event.getEntity() instanceof Player) {
                    Player target = (Player) event.getEntity();
                    if (peopleTeam.hasEntry(target.getName())) {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0, false, false));
                    }
                }
            }
            else if (superMushroomTeam.hasEntry(player.getName())) {
                if (event.getEntity() instanceof Player) {
                    Player target = (Player) event.getEntity();
                    if (peopleTeam.hasEntry(target.getName())) {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 0, false, false));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFriendlyMushroom(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (mushroomTeam.hasEntry(player.getName())) {
                if (event.getEntity() instanceof Player) {
                    Player target = (Player) event.getEntity();
                    if (superMushroomTeam.hasEntry(target.getName())) {
                        event.setCancelled(true);
                    }
                }
            }
            else if (superMushroomTeam.hasEntry(player.getName())) {
                if (event.getEntity() instanceof Player) {
                    Player target = (Player) event.getEntity();
                    if (mushroomTeam.hasEntry(target.getName())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
