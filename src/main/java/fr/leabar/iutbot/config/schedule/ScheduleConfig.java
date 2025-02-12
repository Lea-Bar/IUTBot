package fr.leabar.iutbot.config.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScheduleConfig {
    private String urlS1;
    private String urlS2;
    private int roleId;
    private String schoolClass;
}
