package com.snippet.gig.controller;

import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.calendar.ICalendarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("${api.prefix}/calendar")
@Tag(name = "Calendar APIs")
class CalendarController {
    private final ICalendarService calendarService;

    CalendarController(ICalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/public/get-calendar-feed")
    public ResponseEntity<ApiResponse> getCalendarFeed(
            @RequestParam String username
    ) {
        try {
            String data = calendarService.getCalendarFeed(username);
            return ResponseEntity.ok(
                    new ApiResponse(
                            "email sent Successfully",
                            data
                    ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse(
                            "Failed to generate calendar feed",
                            e.getMessage()
                    ));
        }
    }

    @GetMapping("/download/{username}")
    public void downloadCalendar(@PathVariable String username, HttpServletResponse response) {
        try {
            // Generate calendar content
            String calendarContent = calendarService.getCalendarFeed(username);

            // Set response headers to download file
            response.setContentType("text/calendar");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + username + "_calendar.ics\"");

            // Write calendar content to response
            response.getWriter().write(calendarContent);
        } catch (ResourceNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
