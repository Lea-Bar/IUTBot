package fr.leabar.iutbot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.leabar.iutbot.config.discord.DiscordConfig;
import fr.leabar.iutbot.utils.Tuple;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.lang.reflect.Field;

public class ConfigManager {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Getter
    private static ConfigManager instance;

    @Getter
    private DiscordConfig discordConfig;

    public ConfigManager(){
        instance = this;
    }

    public boolean loadAllConfig(){
        Tuple<DiscordConfig, Boolean> discordConfigTuple = loadFile("./config/discord.json", DiscordConfig.class);
        this.discordConfig = discordConfigTuple.getFirstElement();
        return discordConfigTuple.getSecondElement();
    }

    @SneakyThrows
    private <T> Tuple<T, Boolean> loadFile(String path, Class<T> objectClass){
        File file = new File(path);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(!file.exists()){
            T obj = objectClass.getConstructor().newInstance();
            Writer writer = new FileWriter(path);
            GSON.toJson(obj, writer);
            writer.flush();
            writer.close();
            return new Tuple<>(obj, false);
        }
        Reader reader = new FileReader(path);
        T obj = GSON.fromJson(reader, objectClass);
        reader.close();
        return new Tuple<>(obj, true);
    }

}
