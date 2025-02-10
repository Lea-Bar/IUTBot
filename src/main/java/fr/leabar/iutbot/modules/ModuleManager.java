package fr.leabar.iutbot.modules;

import fr.leabar.iutbot.modules.config.ConfigModule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManager {
    private static final Set<IModule> MODULES = Collections.newSetFromMap(new ConcurrentHashMap<>());


    public static void registerModules(){
        MODULES.add(new ConfigModule());
    }

    public static void startModules(){
        MODULES.forEach(IModule::start);
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

    public static Set<IModule> getModules() {
        return Collections.unmodifiableSet(MODULES);
    }
}