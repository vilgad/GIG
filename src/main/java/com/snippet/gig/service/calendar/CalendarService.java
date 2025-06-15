package com.snippet.gig.service.calendar;

import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CalendarService implements ICalendarService {
    private final UserRepository userRepository;

    public CalendarService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getCalendarFeed(String username) throws ResourceNotFoundException, IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        log.info("user fetched: {}", user.getUsername());
        if (user.getTasks() == null || user.getTasks().isEmpty()) {
            log.warn("No tasks found for user: {}", username);
            return "No tasks available for the user.";
        }

        List<Task> tasks = user.getTasks();

        log.info("creating calendar feed for user: {}", username);
        Calendar calendar = new Calendar();
        log.info("calendar object created for user: {}", username);
        calendar.getProperties().add(new ProdId("-//GIG/EN"));
        log.info("proid added to calendar for user: {}", username);
        calendar.getProperties().add(Version.VERSION_2_0);
        log.info("version added to calendar for user: {}", username);
        calendar.getProperties().add(CalScale.GREGORIAN);
//        calendar.getProperties().add(new CalScale("GREGORIAN"));
        log.info("calscale added to calendar for user: {}", username);

        for (Task task : tasks) {
            log.info("Processing task: {}", task.getTitle());
            if (task.getDueDate() == null) continue;

            log.info("Adding task to calendar: {}", task.getTitle());

            LocalDateTime start = task.getDueDate().atTime(10, 0);
            LocalDateTime end = start.plusHours(1);

            VEvent event = new VEvent(
                    new DateTime(java.util.Date.from(start.atZone(ZoneId.systemDefault()).toInstant())),
                    new DateTime(java.util.Date.from(end.atZone(ZoneId.systemDefault()).toInstant())),
                    task.getTitle()
            );

            event.getProperties().add(new Uid(UUID.randomUUID().toString()));
            event.getProperties().add(new Description(task.getDescription()));

            calendar.getComponents().add(event);
        }

        log.info("Calendar feed created successfully for user: {}", username);
        StringWriter writer = new StringWriter();
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, writer);

        log.info("Calendar feed output generated for user: {}", username);
        return writer.toString();
    }
}