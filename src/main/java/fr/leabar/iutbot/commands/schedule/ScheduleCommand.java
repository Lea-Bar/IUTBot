package fr.leabar.iutbot.commands.schedule;

import fr.leabar.iutbot.commands.SlashCommand;
import fr.leabar.iutbot.schedule.Schedule;
import fr.leabar.iutbot.schedule.lesson.Lesson;
import fr.leabar.iutbot.schedule.manager.ScheduleManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ScheduleCommand {
    private static final int CACHE_DURATION_MINUTES = 30;
    private static final Map<String, DayOfWeek> DAYS_MAP = new HashMap<>() {{
        put("Lundi", DayOfWeek.MONDAY);
        put("Mardi", DayOfWeek.TUESDAY);
        put("Mercredi", DayOfWeek.WEDNESDAY);
        put("Jeudi", DayOfWeek.THURSDAY);
        put("Vendredi", DayOfWeek.FRIDAY);
        put("Samedi", DayOfWeek.SATURDAY);
    }};

    @SlashCommand(
            name = "edt",
            description = "Visionne ton emploi du temps du jour",
            options = {"DATE:date:Date du jour:false", "CLASS:classe:Classe:false"}
    )
    public void edt(SlashCommandInteraction event) {
        Schedule schedule = findSchedule(event);

        if (schedule == null) {
            event.reply(":x: **L'emploi du temps n'a pas été trouvé** :x:").queue();
            return;
        }

        updateScheduleIfNeeded(schedule);
        LocalDate date = determineRequestedDate(event);
        List<Lesson> lessons = schedule.getLessonsByDate(date);

        EmbedBuilder embedBuilder = createScheduleEmbed(date, lessons);
        event.replyEmbeds(embedBuilder.build()).queue();
    }

    private Schedule findSchedule(SlashCommandInteraction event) {
        OptionMapping classOption = event.getOption("classe");
        if (classOption != null) {
            return ScheduleManager.getInstance()
                    .getSchedule(classOption.getAsString())
                    .orElse(null);
        }

        return event.getMember().getRoles().stream()
                .map(role -> ScheduleManager.getInstance().getSchedule(Long.parseLong(role.getId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(null);
    }

    private void updateScheduleIfNeeded(Schedule schedule) {
        long currentTime = System.currentTimeMillis();
        long cacheExpiration = schedule.getLastTimeLoaded() + (CACHE_DURATION_MINUTES * 60 * 1000);

        if (!schedule.isLoaded() || currentTime >= cacheExpiration) {
            ScheduleManager.getInstance().loadSchedule(schedule);
            schedule.setLoaded(true);
            schedule.setLastTimeLoaded(currentTime);
        }
    }

    private LocalDate determineRequestedDate(SlashCommandInteraction event) {
        OptionMapping dateOption = event.getOption("date");
        return dateOption != null ?
                getDateFromDayString(dateOption.getAsString()) :
                LocalDate.now();
    }

    private EmbedBuilder createScheduleEmbed(LocalDate date, List<Lesson> lessons) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Emploi du temps du " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        if (lessons.isEmpty()) {
            return embedBuilder
                    .setColor(0xe74c3c)
                    .addField("Aucun cours", "Aujourd'hui c'est rompish !", false);
        }

        embedBuilder.setColor(0x27ae60);
        lessons.forEach(lesson -> addLessonField(embedBuilder, lesson));
        return embedBuilder;
    }

    private void addLessonField(EmbedBuilder embedBuilder, Lesson lesson) {
        String timeRange = String.format("%02d:%02d - %02d:%02d",
                lesson.getStart().getHour(), lesson.getStart().getMinute(),
                lesson.getEnd().getHour(), lesson.getEnd().getMinute());

        String teacher = lesson.getTeacher().isEmpty() ? "Aucun" : lesson.getTeacher();
        String room = lesson.getRoom().isEmpty() ? "Aucun" : lesson.getRoom();

        String description = String.format("Horaire: %s%nEnseignant: %s%nSalle: %s",
                timeRange, teacher, room);

        embedBuilder.addField(lesson.getSubject(), description, false);
    }

    public static LocalDate getDateFromDayString(String day) {
        DayOfWeek targetDay = DAYS_MAP.get(day);
        if (targetDay == null) {
            return LocalDate.now();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        return startOfWeek.plusDays(targetDay.getValue() - DayOfWeek.MONDAY.getValue());
    }
}