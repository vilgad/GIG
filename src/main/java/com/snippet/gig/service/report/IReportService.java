package com.snippet.gig.service.report;

import com.snippet.gig.exception.ResourceNotFoundException;

public interface IReportService {
    byte[] generateUserProductivityPdf(String username) throws ResourceNotFoundException;
}
