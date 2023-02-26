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
                Bukkit.getLogger().info("§a최신 버전을 사용 중 입니다.");
            } else {
                Bukkit.getLogger().info("§c플러그인이 최신 버전이 아닙니다. https://github.com/yj0524/PoisonousMushroom/releases/latest/download/PoisonousMushroom.jar 에서 다운로드 해 주세요.");
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
                    Bukkit.getLogger().info("§a최신 버전을 사용 중 입니다.");
                } else {
                    Bukkit.getLogger().info("§c플러그인이 최신 버전이 아닙니다. https://github.com/yj0524/PoisonousMushroom/releases/latest/download/PoisonousMushroom.jar 에서 다운로드 해 주세요.");
                }
            } catch (Throwable e) {
                Bukkit.getLogger().info("§c업데이트 확인에 실패했습니다.");
            }
        }
    }
}