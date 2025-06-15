package com.snippet.gig.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snippet.gig.enums.Priority;
import com.snippet.gig.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;
    private String description;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "tasks", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<User> users;

    //    @JsonProperty(value = "project")
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @JsonIgnore
    @OneToMany
    List<Comment> comments = new ArrayList<>();

    // jo getter present hote hai whi json response mei aate hai
    public String getProjectName() {
        return project != null ? project.getName() : null;
    }

    public Task(String title, String description, LocalDate dueDate, Priority priority, Status status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }
}
