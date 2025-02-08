package fr.leabar.iutbot;

import fr.leabar.iutbot.config.ConfigManager;
import fr.leabar.iutbot.modules.ModuleManager;

public class IUTBot {
    public static void main(String[] args) {
        ModuleManager.registerModules();
        ModuleManager.startModules();
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run() {
                ModuleManager.stopModules();
            }
        });
    }
}