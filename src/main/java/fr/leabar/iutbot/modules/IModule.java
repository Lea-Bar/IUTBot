package fr.leabar.iutbot.modules;

import java.util.concurrent.CompletableFuture;

public interface IModule {
    CompletableFuture<Boolean> start();

    void stop();
}
