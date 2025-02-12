package fr.leabar.iutbot.schedule.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class Lesson {
    private final String lessonName, teacherName, room;
    private final LocalTime startTime, endTime;
}
