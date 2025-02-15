package fr.leabar.iutbot.schedule;

import fr.leabar.iutbot.schedule.lesson.Lesson;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Schedule {
    private final List<Lesson> lessons;
    private final String schoolClass;
    private final int roleID;
    private final boolean loaded;
    private final long lastTimeLoaded;

    public Schedule(String schoolClass, int roleID) {
        this.lessons = new ArrayList<>();
        this.schoolClass = schoolClass;
        this.roleID = roleID;
        this.loaded = false;
        this.lastTimeLoaded = 0;
    }

    public List<Lesson> getLessonsByDate(LocalDate date) {
        return lessons.stream()
                .filter(lesson -> lesson.getStart().toLocalDate().equals(date))
                .sorted((l1,l2) -> l1.getStart().compareTo(l2.getStart()))
                .collect(Collectors.toList());
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

}
