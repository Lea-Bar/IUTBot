package fr.leabar.iutbot.utils;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.ICalDate;
import fr.leabar.iutbot.schedule.lesson.Lesson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ScheduleUtils {

    public static Lesson eventToLesson(VEvent event) {
        String subject = event.getSummary().getValue();
        String teacher = "";
        if(event.getDescription() != null){
            String[] description = event.getDescription().getValue().split("\n");
            if(description.length > 1){
                teacher = description[1].replace("Enseignant : ", "").trim();
            }
        }
        String room = "";
        if(event.getLocation() != null){
            room = event.getLocation().getValue();
        }
        LocalDateTime start = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return new Lesson(subject, teacher, room, start, end);
    }

    public static List<VEvent> getAllEventsFromURL(OkHttpClient httpClient, String url, Date startDate, Date endDate) throws IOException {
        List<VEvent> events = getAllEventsFromURL(httpClient, url);
        return events.stream().filter((event) -> {
           ICalDate date = event.getDateStart().getValue();
           return !date.before(startDate) && date.before(endDate);
        }).toList();
    }

    private static List<VEvent> getAllEventsFromURL(OkHttpClient httpClient, String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = httpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            return null;
        }
        ResponseBody body = response.body();
        if(body == null){
            return null;
        }
        ICalendar ical = Biweekly.parse(body.string()).first();
        return ical.getEvents();
    }

}
