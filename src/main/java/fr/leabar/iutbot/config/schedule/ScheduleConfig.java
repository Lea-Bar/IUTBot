package fr.leabar.iutbot.config.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScheduleConfig {
    private String url;
    private int roleId;
    private String schoolClass;
}
