package com.github.yj0524.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class UpdateChecker {
    public static void check(JavaPlugin plugin, String user, String repo) {
        String currentversion = plugin.getDescription().getVersion();
        Bukkit.getLogger().info("Checking update...");
        try {
            URL url = new URL(Objects.requireNonNull(
                    Objects.requireNonNull("https://raw.githubusercontent.com/user-name/repo-name/master/version.txt")
                            .replace("user-name", user).replace("repo-name", repo)));
            InputStream is = url.openStream();
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            String version = br.readLine();
            if (version.equals(currentversion)) {
                Bukkit.getLogger().info("You are using the latest version of the plugin!");
            } else {
                Bukkit.getLogger().info("Outdated plugin!");
                Bukkit.getLogger().info("Please go to plugin page and download the latest version!");
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
                    Bukkit.getLogger().info("You are using the latest version of the plugin!");
                } else {
                    Bukkit.getLogger().info("Outdated plugin!");
                    Bukkit.getLogger().info("Please go to plugin page and download the latest version!");
                }
            } catch (Throwable e) {
                Bukkit.getLogger().info("Error checking plugin update!");
            }
        }
    }
}