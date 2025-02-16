package fr.leabar.iutbot.schedule.manager;

import fr.leabar.iutbot.config.ConfigManager;
import fr.leabar.iutbot.config.schedule.ScheduleWrapper;
import fr.leabar.iutbot.schedule.Schedule;
import fr.leabar.iutbot.utils.ScheduleUtils;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;

public class ScheduleManager {
    private final List<Schedule> SCHEDULES = new ArrayList<Schedule>();
    private static volatile ScheduleManager instance;
    private static final Object LOCK = new Object();
    private final OkHttpClient CLIENT = new OkHttpClient();
    private final Date startDate, endDate;

    public ScheduleManager() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                .withHour(0).withMinute(0).withSecond(0);
        LocalDateTime weekEnd = weekStart.plusDays(7);

        this.startDate = Date.from(weekStart.atZone(ZoneId.systemDefault()).toInstant());
        this.endDate = Date.from(weekEnd.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static ScheduleManager getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ScheduleManager();
                }
            }
        }
        return instance;
    }

    public void loadSchedules() {
        synchronized (LOCK) {
            ScheduleWrapper wrapper = ConfigManager.getInstance().getScheduleWrapper();
            wrapper.getScheduleConfigs().forEach(scheduleConfig -> {
                Schedule schedule = new Schedule(scheduleConfig.getSchoolClass(), scheduleConfig.getRoleId());
                SCHEDULES.add(schedule);
            });
        }
    }

    public void loadSchedule(Schedule schedule) {
        ScheduleWrapper scheduleWrapper = ConfigManager.getInstance().getScheduleWrapper();
        boolean isS2 = ConfigManager.getInstance().getDiscordConfig().isSemester2Enabled();
        scheduleWrapper.getScheduleConfigs().stream().filter(scheduleConfig -> scheduleConfig.getSchoolClass().equals(schedule.getSchoolClass())).forEach(scheduleConfig -> {
            try {
                ScheduleUtils.getAllEventsFromURL(CLIENT,
                        isS2 ? scheduleConfig.getUrlS2() : scheduleConfig.getUrlS1(),
                        startDate, endDate).stream()
                        .map(event -> ScheduleUtils.eventToLesson(event))
                        .forEach(schedule::addLesson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Schedule> getSchedules() {
        return SCHEDULES;
    }

    public Optional<Schedule> getSchedule(String schoolClass) {
        return SCHEDULES.stream().filter(schedule -> schedule.getSchoolClass().equals(schoolClass)).findFirst();
    }

    public Optional<Schedule> getSchedule(long roleID) {
        return SCHEDULES.stream().filter(schedule -> schedule.getRoleID() == roleID).findFirst();
    }
}
