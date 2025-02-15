package fr.leabar.iutbot.modules;

import fr.leabar.iutbot.modules.config.ConfigModule;
import fr.leabar.iutbot.modules.discord.CommandsModule;
import fr.leabar.iutbot.modules.discord.BotModule;
import fr.leabar.iutbot.modules.schedule.ScheduleModule;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class ModuleManager {
    private static final List<IModule> MODULES = new ArrayList<>();

    public static void registerModules(){
        MODULES.add(new ConfigModule());
        MODULES.add(new ScheduleModule());
        MODULES.add(new BotModule());
        MODULES.add(new CommandsModule());
    }

    public static void startModules(){
        for(IModule module : MODULES){
            try {
                if(!module.start().get()){
                    module.stop();
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void stopModules(){
        MODULES.forEach(IModule::stop);
    }

    public static <T extends IModule> Optional<T> getModule(Class<T> tClass){
        return MODULES.stream()
                .filter(tClass::isInstance)
                .map(tClass::cast)
                .findFirst();
    }

    public static List<IModule> getModules() {
        return MODULES;
    }
}