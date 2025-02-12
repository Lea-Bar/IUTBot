package fr.leabar.iutbot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.leabar.iutbot.config.discord.DiscordConfig;
import fr.leabar.iutbot.config.schedule.ScheduleWrapper;
import fr.leabar.iutbot.utils.Tuple;
import lombok.Getter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static final String CONFIG_DIR = "./config";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static volatile ConfigManager instance;
    private static final Object LOCK = new Object();

    @Getter
    private DiscordConfig discordConfig;
    @Getter
    private ScheduleWrapper scheduleWrapper;

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public boolean loadAllConfig() {
        try {
            createConfigDirectory();
            Tuple<DiscordConfig, Boolean> discordConfigTuple = loadConfig("discord.json", DiscordConfig.class);
            Tuple<ScheduleWrapper, Boolean> scheduleWrapperTuple = loadConfig("schedules.json", ScheduleWrapper.class);
            this.discordConfig = discordConfigTuple.getFirstElement();
            this.scheduleWrapper = scheduleWrapperTuple.getFirstElement();
            return discordConfigTuple.getSecondElement() && scheduleWrapperTuple.getSecondElement();
        } catch (IOException e) {
            System.out.println("Failed to load configurations "+e);
            return false;
        }
    }

    public <T> Tuple<T, Boolean> loadConfig(String filename, Class<T> configClass) throws IOException {
        Path path = Paths.get(CONFIG_DIR, filename);
        if (!Files.exists(path)) {
            return createDefaultConfig(path, configClass);
        }
        return loadExistingConfig(path, configClass);
    }

    private void createConfigDirectory() throws IOException {
        Path configDir = Paths.get(CONFIG_DIR);
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }
    }

    private <T> Tuple<T, Boolean> loadConfig(Path path, Class<T> configClass) throws IOException {
        if (!Files.exists(path)) {
            return createDefaultConfig(path, configClass);
        }
        return loadExistingConfig(path, configClass);
    }

    private <T> Tuple<T, Boolean> createDefaultConfig(Path path, Class<T> configClass) throws IOException {
        T defaultConfig;
        try {
            defaultConfig = configClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IOException("Failed to create default configuration instance", e);
        }

        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(defaultConfig, writer);
            return new Tuple<>(defaultConfig, false);
        }
    }

    private <T> Tuple<T, Boolean> loadExistingConfig(Path path, Class<T> configClass) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            T config = GSON.fromJson(reader, configClass);
            if (config == null) {
                throw new IOException("Failed to parse configuration file: " + path);
            }
            return new Tuple<>(config, true);
        }
    }

    public void saveConfig(Object config, String filename) throws IOException {
        Path path = Paths.get(CONFIG_DIR, filename);
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(config, writer);
        }
    }
}