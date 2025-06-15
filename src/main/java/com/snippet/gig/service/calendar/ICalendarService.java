package com.snippet.gig.service.calendar;

import com.snippet.gig.exception.ResourceNotFoundException;

import java.io.IOException;

public interface ICalendarService {
    String getCalendarFeed(String username) throws ResourceNotFoundException, IOException;
}
