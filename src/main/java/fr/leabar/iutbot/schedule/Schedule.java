package fr.leabar.iutbot.schedule;

import fr.leabar.iutbot.schedule.lesson.Lesson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@RequiredArgsConstructor
public class Schedule {
    private final Set<Lesson> lessons = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final String schoolClass;

}
