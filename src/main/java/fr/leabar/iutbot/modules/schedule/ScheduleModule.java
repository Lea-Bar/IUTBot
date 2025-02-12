package fr.leabar.iutbot.modules.schedule;

import fr.leabar.iutbot.modules.IModule;
import okhttp3.OkHttpClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ScheduleModule implements IModule {

    private final OkHttpClient httpClient;
    private final Date startDate, endDate;

    public ScheduleModule() {
        this.httpClient = new OkHttpClient();

        //
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                .withHour(0).withMinute(0).withSecond(0);
        LocalDateTime weekEnd = weekStart.plusDays(7);

        this.startDate = Date.from(weekStart.atZone(ZoneId.systemDefault()).toInstant());
        this.endDate = Date.from(weekEnd.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public CompletableFuture<Boolean> start() {
        return null;
    }

    @Override
    public void stop() {

    }
}
