package com.snippet.gig.service.report;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.TaskRepository;
import com.snippet.gig.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService implements IReportService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ReportService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public byte[] generateUserProductivityPdf(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
        List<Task> tasks = user.getTasks();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        document.addTitle("User Productivity Report");

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Productivity Report for: " + user.getName(), titleFont));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(8);
        table.addCell("Project");
        table.addCell("Task");
        table.addCell("Status");
        table.addCell("Priority");
        table.addCell("Created At");
        table.addCell("Due Date");
        table.addCell("Completed At");
        table.addCell("Completion Time (hrs)");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Task task : tasks) {
            table.addCell(task.getProjectName());
            table.addCell(task.getTitle());
            table.addCell(task.getStatus().name());
            table.addCell(task.getPriority().name());
            table.addCell(task.getCreatedAt().format(dtf));
            table.addCell(task.getDueDate().format(dtf));

            if (task.getCompletedAt() != null && task.getCreatedAt() != null) {
                table.addCell(dtf.format(task.getCompletedAt()));
                Duration duration = Duration.between(task.getCreatedAt(), task.getCompletedAt());
                table.addCell(String.valueOf(duration.toHours()));
            } else {
                table.addCell("N/A");
                table.addCell("N/A");
            }
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
