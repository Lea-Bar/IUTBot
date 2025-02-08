package fr.leabar.iutbot.modules;

import fr.leabar.iutbot.modules.config.ConfigModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleManager {
    private static final List<IModule> MODULES = new ArrayList<>();

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
}