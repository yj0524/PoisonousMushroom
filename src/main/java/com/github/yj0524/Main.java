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
import org.bukkit.inventory.meta.FireworkMeta;
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

    public World overworld = Bukkit.getWorld("world");
    public World nether = Bukkit.getWorld("world_nether");
    public World end = Bukkit.getWorld("world_the_end");

    public Team spectatorTeam;
    public Team sacrificeTeam;
    public Team mushroomTeam;
    public Team superMushroomTeam;
    public Team peopleTeam;

    public boolean isGameEnd = false;
    public boolean isTimerStart = false;
    public final float[] damageMultipliers = { 1.2f, 1.4f, 1.6f, 1.8f, 2.0f };
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
    public float huskTridentPercent;
    public int worldBorderSize;
    public boolean worldBorderEnable;
    public boolean endGateway;
    public boolean randomSpawn;
    public float peopleHealth;
    public float mushroomHealth;
    public float superMushroomHealth;
    public float sacrificePercent;
    public float infectionPercent;
    public boolean infectionEnable;
    public boolean gameEndMessageEnable;
    public boolean informationEnable;
    public float foodLevel;
    public int mushroomAppearMinutes;
    public int mushroomAppearSeconds;

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
        getCommand("getcoordinate").setExecutor(new GetCoordinate(this));

        getCommand("poisonousmushroom").setTabCompleter(new TabCom());
        getCommand("util").setTabCompleter(new UtilTabCom());
        getCommand("update").setTabCompleter(new UpdateTabCom());
        getCommand("configreload").setTabCompleter(new ConfigReloadTabCom());
        getCommand("evolve").setTabCompleter(new EvolveTabCom());
        getCommand("attach").setTabCompleter(new AttachTabCom());
        getCommand("sacrifice").setTabCompleter(new SacrificeTabCom());
        getCommand("getcoordinate").setTabCompleter(new GetCoordinateTabCom());

        loadWorlds();

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

        if (mushroomAppearMinutes == 0 && mushroomAppearSeconds == 0) {
            mushroomAppearMinutes = 30;
            mushroomAppearSeconds = 0;
        }

        overworld.setGameRule(GameRule.DO_MOB_SPAWNING, mobSpawn);
        overworld.setGameRule(GameRule.FALL_DAMAGE, false);
        overworld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

        nether.setGameRule(GameRule.DO_MOB_SPAWNING, mobSpawn);
        nether.setGameRule(GameRule.FALL_DAMAGE, false);
        nether.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

        end.setGameRule(GameRule.DO_MOB_SPAWNING, mobSpawn);
        end.setGameRule(GameRule.FALL_DAMAGE, false);
        end.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

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

        new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld("world");

                if (world.getTime() < 12000) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (mushroomTeam.hasEntry(player.getName()) || superMushroomTeam.hasEntry(player.getName())) {
                            player.removePotionEffect(PotionEffectType.SPEED);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, -1, 0, false, false));
                        }
                    }
                }
                else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (mushroomTeam.hasEntry(player.getName()) || superMushroomTeam.hasEntry(player.getName())) {
                            player.removePotionEffect(PotionEffectType.SLOW);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 0, false, false));
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setPlayerListHeader("§a" + getPluginMeta().getName() + " Version " + version + "\nMade by yj0524_kr");
                    player.setPlayerListFooter("Ping : " + player.getPing() + "ms\nServer Memory : " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "MB / " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
                }
            }
        }.runTaskTimer(this, 0, 1);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled");

        isGameEnd = false;

        configSave();
    }

    public void configSave() {
        FileConfiguration config = getConfig();
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
        config.set("gameEndMessageEnable", gameEndMessageEnable);
        config.set("informationEnable", informationEnable);
        config.set("mushroomAppearMinutes", mushroomAppearMinutes);
        config.set("mushroomAppearSeconds", mushroomAppearSeconds);
    }

    public void loadWorlds() {
        overworld = Bukkit.getWorld("world");
        nether = Bukkit.getWorld("world_nether");
        end = Bukkit.getWorld("world_the_end");
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
        respawnsemaphoreRecipe.shape(" I ", "EDC", " G ");
        respawnsemaphoreRecipe.setIngredient('I', Material.IRON_INGOT);
        respawnsemaphoreRecipe.setIngredient('E', Material.EMERALD);
        respawnsemaphoreRecipe.setIngredient('D', Material.DIAMOND);
        respawnsemaphoreRecipe.setIngredient('C', Material.COPPER_INGOT);
        respawnsemaphoreRecipe.setIngredient('G', Material.GOLD_INGOT);
        Bukkit.addRecipe(respawnsemaphoreRecipe);

        // 포자 퇴치기 레시피
        ItemStack vaccine = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta vaccineMeta = vaccine.getItemMeta();
        vaccineMeta.setDisplayName("§c포자 퇴치기");
        vaccine.setItemMeta(vaccineMeta);
        NamespacedKey key = new NamespacedKey(this, "vaccine");
        ShapedRecipe vaccineRecipe = new ShapedRecipe(key, vaccine);
        vaccineRecipe.shape("ORE", "TBT", "ANC");
        vaccineRecipe.setIngredient('O', Material.BROWN_MUSHROOM);
        vaccineRecipe.setIngredient('R', new ItemStack(respawnsemaphore));
        vaccineRecipe.setIngredient('E', Material.RED_MUSHROOM);
        vaccineRecipe.setIngredient('T', Material.TRIDENT);
        vaccineRecipe.setIngredient('B', Material.GLASS_BOTTLE);
        vaccineRecipe.setIngredient('A', Material.GOLDEN_APPLE);
        vaccineRecipe.setIngredient('N', Material.NETHERITE_INGOT);
        vaccineRecipe.setIngredient('C', Material.GOLDEN_CARROT);
        Bukkit.addRecipe(vaccineRecipe);
    }

    private void setWorldBorder() {
        if (worldBorderEnable) {
            WorldBorder worldBorder_over = overworld.getWorldBorder();
            worldBorder_over.setCenter(0, 0);
            worldBorder_over.setSize(worldBorderSize);

            WorldBorder worldBorder_nether = nether.getWorldBorder();
            worldBorder_nether.setCenter(0, 0);
            worldBorder_nether.setSize(worldBorderSize);

            if (!endGateway) {
                WorldBorder worldBorder_end = end.getWorldBorder();
                worldBorder_end.setCenter(0, 0);
                worldBorder_end.setSize(512);
            }
        }
    }

    private void loadScoreboard() {
        if (informationEnable) {
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
    }

    public void loadConfig() {
        // Load config
        FileConfiguration config = getConfig();
        huskHealth = config.getInt("huskHealth", 20);
        huskCount = config.getInt("huskCount", 10);
        mushroomPlayerName = config.getString("mushroomPlayerName", "yj0524_kr");
        serverAutoShutDown = config.getBoolean("serverAutoShutDown", false);
        serverShutDownTick = config.getInt("serverShutDownTick", 60);
        mobFollowRange = config.getInt("mobFollowRange", 128);
        respawnSpectatorRange = config.getInt("respawnSpectatorRange", 15);
        mobSpawn = config.getBoolean("mobSpawn", true);
        huskTridentPercent = (float) config.getDouble("huskTridentPercent", 0.3f);
        worldBorderSize = config.getInt("worldBorderSize", 1024);
        worldBorderEnable = config.getBoolean("worldBorderEnable", true);
        endGateway = config.getBoolean("endGateway", true);
        randomSpawn = config.getBoolean("randomSpawn", true);
        peopleHealth = (float) config.getDouble("peopleHealth", 20.0f);
        mushroomHealth = (float) config.getDouble("mushroomHealth", 20.0f);
        superMushroomHealth = (float) config.getDouble("superMushroomHealth", 40.0f);
        sacrificePercent = (float) config.getDouble("sacrificePercent", 30.0f);
        infectionPercent = (float) config.getDouble("infectionPercent", 5.0f);
        infectionEnable = config.getBoolean("infectionEnable", true);
        gameEndMessageEnable = config.getBoolean("gameEndMessageEnable", false);
        informationEnable = config.getBoolean("informationEnable", false);
        mushroomAppearMinutes = config.getInt("mushroomAppearMinutes", 30);
        mushroomAppearSeconds = config.getInt("mushroomAppearSeconds", 0);
        // Save config
        configSave();
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
            if (!peopleTeam.hasEntry(player.getName()) && !spectatorTeam.hasEntry(player.getName()) && !sacrificeTeam.hasEntry(player.getName()) && !mushroomTeam.hasEntry(player.getName()) && !superMushroomTeam.hasEntry(player.getName())) {
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
                for (Player allplayers : Bukkit.getOnlinePlayers()) {
                    if (randomSpawn) {
                        allplayers.sendMessage("§a버섯 " + event.getPlayer().getName() + "이(가) 죽었습니다! 월드의 랜덤 스폰 위치로 이동했습니다!");
                    }
                    else if (!randomSpawn) {
                        allplayers.sendMessage("§a버섯 " + event.getPlayer().getName() + "이(가) 죽었습니다! 월드의 스폰으로 이동했습니다!");
                    }
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
                            allplayers.sendTitle("§c게임 종료", "§a포자 퇴치기를 만들었습니다. " + (serverShutDownTick / 20) + "초 후에 서버가 종료됩니다.");
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
                            // mushroomPlayerName 플레이어의 위치에 별 모양 폭죽을 터뜨림
                            if (mushroomPlayerName != null) {
                                Player mushroomPlayer = Bukkit.getPlayer(mushroomPlayerName);
                                if (mushroomPlayer != null) {
                                    World world = mushroomPlayer.getWorld();
                                    Location location = mushroomPlayer.getLocation();

                                    FireworkEffect effect = FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.STAR).build();
                                    Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK);
                                    FireworkMeta meta = firework.getFireworkMeta();
                                    meta.addEffect(effect);
                                    meta.setPower(1);
                                    firework.setFireworkMeta(meta);
                                }
                            }
                            if (gameEndMessageEnable) {
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
                            // mushroomPlayerName 플레이어의 위치에 별 모양 폭죽을 터뜨림
                            if (mushroomPlayerName != null) {
                                Player mushroomPlayer = Bukkit.getPlayer(mushroomPlayerName);
                                if (mushroomPlayer != null) {
                                    World world = mushroomPlayer.getWorld();
                                    Location location = mushroomPlayer.getLocation();

                                    FireworkEffect effect = FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.STAR).build();
                                    Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK);
                                    FireworkMeta meta = firework.getFireworkMeta();
                                    meta.addEffect(effect);
                                    meta.setPower(1);
                                    firework.setFireworkMeta(meta);
                                }
                            }
                            if (gameEndMessageEnable) {
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
                                    if (gameEndMessageEnable) {
                                        for (Player allplayers1 : Bukkit.getOnlinePlayers()) {
                                            allplayers1.sendMessage("§c버섯은 사라졌지만, 이미 인간도 감염되어있었다...");
                                        }
                                    }
                                }, 200);
                            }
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
                    int tmpInt = 0;
                    String tmpPlayerName = "";

                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        allplayers.sendMessage("§a" + player.getName() + " 님이 부활 신호기를 사용했습니다!");
                    }

                    // 사용한 사람 기준 주변 10블록 관전자에게 People 팀으로 Join
                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        if (allplayers.getGameMode() == GameMode.SPECTATOR && spectatorTeam.hasEntry(allplayers.getName())) {
                            if (allplayers.getLocation().distance(player.getLocation()) <= respawnSpectatorRange) {
                                allplayers.setGameMode(GameMode.SURVIVAL);
                                spectatorTeam.removeEntry(allplayers.getName());
                                peopleTeam.addEntry(allplayers.getName());
                                tmpPlayerName = allplayers.getName();
                                tmpInt++;
                            }
                        }
                        for (Player allplayers1 : Bukkit.getOnlinePlayers()) {
                            if (tmpInt != 0) {
                                allplayers1.sendMessage("§a" + tmpPlayerName + "님이 부활했습니다!");
                                allplayers1.playSound(allplayers1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            }
                        }
                    }

                    for (Player allplayers : Bukkit.getOnlinePlayers()) {
                        if (tmpPlayer == peopleTeam.getSize()) {
                            allplayers.sendMessage("§c하지만, 주변에 영혼이 없어서 부활에 실패했습니다!");
                            allplayers.playSound(allplayers.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
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
            if (event.getEntityType() == EntityType.ZOMBIE || event.getEntityType() == EntityType.HUSK || event.getEntityType() == EntityType.DROWNED || event.getEntityType() == EntityType.PHANTOM) {
                if (event.getEntityType() == EntityType.ZOMBIE) {
                    Zombie zombie = (Zombie) event.getEntity();
                    zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(mobFollowRange);
                    zombie.getEquipment().setHelmet(new ItemStack(Material.STONE_BUTTON));
                }

                if (event.getEntityType() == EntityType.HUSK) {
                    Husk husk = (Husk) event.getEntity();
                    husk.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(mobFollowRange);
                }

                if (event.getEntityType() == EntityType.PHANTOM) {
                    event.setCancelled(true);
                    return;
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
