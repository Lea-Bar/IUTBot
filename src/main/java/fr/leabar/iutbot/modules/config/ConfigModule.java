package fr.leabar.iutbot.modules.config;

import fr.leabar.iutbot.config.ConfigManager;
import fr.leabar.iutbot.modules.IModule;

public class ConfigModule implements IModule {
    @Override
    public void start() {
        ConfigManager configManager = new ConfigManager();
        configManager.loadAllConfig();
    }

    @Override
    public void stop() {

    }
}