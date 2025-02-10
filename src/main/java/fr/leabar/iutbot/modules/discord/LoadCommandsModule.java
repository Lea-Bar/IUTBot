package fr.leabar.iutbot.modules.discord;

import fr.leabar.iutbot.commands.CommandManager;
import fr.leabar.iutbot.commands.TestCommand;
import fr.leabar.iutbot.events.CommandEvents;
import fr.leabar.iutbot.modules.IModule;
import fr.leabar.iutbot.modules.ModuleManager;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

public class LoadCommandsModule implements IModule {
    @Getter
    private CommandManager manager;

    @Override
    public CompletableFuture<Boolean> start() {
        return CompletableFuture.supplyAsync(() -> {
            this.manager = new CommandManager();
            manager.registerCommands(new TestCommand());
            ModuleManager.getModule(LoadingBotModule.class).ifPresent(module -> {
                module.getJdaInstance().addEventListener(new CommandEvents(manager));
            });
            return true;
        });
    }

    @Override
    public void stop() {

    }
}
