package fr.leabar.iutbot.config.schedule;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class ScheduleWrapper {
    private List<ScheduleConfig> scheduleConfigs;

    public ScheduleWrapper() {
        this.scheduleConfigs = new ArrayList<>();
        this.scheduleConfigs.add(new ScheduleConfig("https://www.exemple.fr/", 100, "S2-C"));
    }

    public Optional<ScheduleConfig> getScheduleByClass(String schoolClass){
        return scheduleConfigs.stream()
                .filter(sc -> sc.getSchoolClass().equalsIgnoreCase(schoolClass))
                .findFirst();
    }

    public Optional<ScheduleConfig> getScheduleByRoleID(int roleID){
        return scheduleConfigs.stream()
                .filter(sc -> sc.getRoleId() == roleID)
                .findFirst();
    }
}
