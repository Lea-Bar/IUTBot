package fr.leabar.iutbot.schedule;

import fr.leabar.iutbot.schedule.lesson.Lesson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@RequiredArgsConstructor
public class Schedule {
    private final ConcurrentHashMap<LocalDate, Lesson> lessons = new ConcurrentHashMap<>();
    private final String schoolClass;

}
