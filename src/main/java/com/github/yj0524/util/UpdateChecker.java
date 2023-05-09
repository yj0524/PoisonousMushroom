package com.github.yj0524.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class UpdateChecker {
    public static void check(JavaPlugin plugin, String user, String repo) {
        String currentversion = plugin.getDescription().getVersion();
        Bukkit.getConsoleSender().sendMessage(ChatColor.WHITE + "Checking update...");
        try {
            URL url = new URL(Objects.requireNonNull(
                    Objects.requireNonNull("https://raw.githubusercontent.com/user-name/repo-name/master/version.txt")
                            .replace("user-name", user).replace("repo-name", repo)));
            InputStream is = url.openStream();
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            String version = br.readLine();
            if (version.equals(currentversion)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "You are using the latest version.");
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Plugin is not up to date. Latest version is " + version + ". Please download from https://github.com/yj0524/PoisonousMushroom/releases/latest/download/PoisonousMushroom.jar");
            }
        } catch (Throwable t) {
            try {
                URL url = new URL(Objects.requireNonNull(
                        Objects.requireNonNull("https://cdn.jsdelivr.net/gh/user-name/repo-name/version.txt")
                                .replace("user-name", user).replace("repo-name", repo)));
                InputStream is = url.openStream();
                InputStreamReader ir = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(ir);
                String version = br.readLine();
                if (version.equals(currentversion)) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "You are using the latest version.");
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Plugin is not up to date. Latest version is " + version + ". Please download from https://github.com/yj0524/PoisonousMushroom/releases/latest/download/PoisonousMushroom.jar");
                }
            } catch (Throwable e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Update check failed. Error Message : " + e.getMessage());
            }
        }
    }
}
