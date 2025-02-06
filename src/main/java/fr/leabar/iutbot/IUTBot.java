package fr.leabar.iutbot;

import fr.leabar.iutbot.config.ConfigManager;

public class IUTBot {
    public static void main(String[] args) {
        ConfigManager configManager = new ConfigManager();
        configManager.loadAllConfig();
    }
}