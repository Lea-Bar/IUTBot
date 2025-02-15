package fr.leabar.iutbot.schedule.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class Lesson {
    private final String subject, teacher, room;
    private final LocalDateTime start, end;
}
