package fr.leabar.iutbot.modules.config;

import fr.leabar.iutbot.config.ConfigManager;
import fr.leabar.iutbot.modules.IModule;

import java.util.concurrent.CompletableFuture;

public class ConfigModule implements IModule {
    @Override
    public CompletableFuture<Boolean> start() {
        return CompletableFuture.supplyAsync(() -> {
            ConfigManager.getInstance().loadAllConfig();
            return true;
        });
    }

    @Override
    public void stop() {

    }
}