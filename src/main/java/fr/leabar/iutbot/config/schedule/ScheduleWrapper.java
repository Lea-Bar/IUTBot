package fr.leabar.iutbot.config.schedule;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class ScheduleWrapper {
    private List<ScheduleConfig> scheduleConfigs;

    public ScheduleWrapper() {
        this.scheduleConfigs = new ArrayList<>();
        this.scheduleConfigs.add(new ScheduleConfig("https://www.exemple.fr/", "https://www.exemple.fr/S2", 100, "S2-C"));
    }

}
