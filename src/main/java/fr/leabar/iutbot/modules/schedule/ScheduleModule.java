package fr.leabar.iutbot.modules.schedule;

import fr.leabar.iutbot.modules.IModule;
import fr.leabar.iutbot.schedule.manager.ScheduleManager;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class ScheduleModule implements IModule {


    @Override
    public CompletableFuture<Boolean> start() {
        return CompletableFuture.supplyAsync(() -> {
            ScheduleManager.getInstance().loadSchedules();
            return true;
        });
    }

    @Override
    public void stop() {

    }
}
