package fr.leabar.iutbot.commands.schedule;

import fr.leabar.iutbot.commands.SlashCommand;
import fr.leabar.iutbot.schedule.Schedule;
import fr.leabar.iutbot.schedule.lesson.Lesson;
import fr.leabar.iutbot.schedule.manager.ScheduleManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ScheduleCommand {
    @SlashCommand(
            name = "edt",
            description = "Visionne ton emploi du temps du jour",
            options = {"DATE:date:Date du jour:false", "CLASS:classe:Classe:false"}
    )
    public void edt(SlashCommandInteraction event){
        Member member = event.getMember();
        Schedule schedule = null;

        if (event.getOptions().isEmpty()) {
            for (Role role : member.getRoles()) {
                System.out.println(role.getId()+" "+role.getName());
                Optional<Schedule> scheduleOptional = ScheduleManager.getInstance().getSchedule(Long.parseLong(role.getId()));
                System.out.println(scheduleOptional.isPresent());
                if (scheduleOptional.isPresent()) {
                    schedule = scheduleOptional.get();
                    break;
                }
            }
        } else {
            if (event.getOption("classe") != null) {
                String classe = event.getOption("classe").getAsString();
                Optional<Schedule> scheduleOptional = ScheduleManager.getInstance().getSchedule(classe);
                if (scheduleOptional.isPresent()) {
                    schedule = scheduleOptional.get();
                }
            }
        }

        if (schedule == null) {
            event.reply(":x: **L'emploi du temps n'a pas été trouvé ** :x:").queue();
            return;
        }

        loadScheduleIfNeeded(schedule);
        LocalDate date = LocalDate.now();

        if (event.getOption("date") != null) {
            date = getDateFromDayString(event.getOption("date").getAsString());
        }

        List<Lesson> lessons = schedule.getLessonsByDate(date);
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Emploi du temps du " + date.toString());

        if (lessons.isEmpty()) {
            embedBuilder.setColor(0xe74c3c)
                    .addField("Aucun cours", "Aujourd'hui c'est rompish !", false);
        } else {
            for (Lesson lesson : lessons) {
                String startHour = String.format("%02d", lesson.getStart().getHour());
                String startMinute = String.format("%02d", lesson.getStart().getMinute());
                String endHour = String.format("%02d", lesson.getEnd().getHour());
                String endMinute = String.format("%02d", lesson.getEnd().getMinute());
                embedBuilder.addField(
                        lesson.getSubject(),
                        "Commence à " + startHour + "h" + startMinute +
                                " jusqu'à " + endHour + "h" + endMinute +
                                "\nEnseignant: " + (lesson.getTeacher().isEmpty() ? "Aucun" : lesson.getTeacher()) +
                                "\nSalle: " + (lesson.getRoom().isEmpty() ? "Aucun" : lesson.getRoom()),
                        false
                );
            }
            embedBuilder.setColor(0x27ae60);
        }

        event.replyEmbeds(embedBuilder.build()).queue();
    }

    private void loadScheduleIfNeeded(Schedule schedule){
        if(!schedule.isLoaded() || schedule.getLastTimeLoaded()+(30*60*1000) <= System.currentTimeMillis()){
            ScheduleManager.getInstance().loadSchedule(schedule);
            schedule.setLoaded(true);
            schedule.setLastTimeLoaded(System.currentTimeMillis());
        }
    }

    public static LocalDate getDateFromDayString(String day) {

        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};

        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();

        int targetIndex = -1;
        for (int i = 0; i < jours.length; i++) {
            if (jours[i].equals(day)) {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) return today;
        int todayIndex = todayDayOfWeek.getValue() - 1;

        if (targetIndex > todayIndex) {
            return today.minusDays(todayIndex + (7 - targetIndex));
        } else {
            return today.minusDays(todayIndex - targetIndex);
        }
    }
}
