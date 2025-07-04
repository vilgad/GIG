package com.snippet.gig.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDate dob;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String username;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // TODO (uncomment this)
//    @Column(unique = true, nullable = false)
    private String telegramUsername;

    private Long telegramChatId;

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_task",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks = new ArrayList<>();

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new HashSet<>();

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "mentionedUsers", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @ToString.Exclude
    private List<Comment> mentionedComments = new ArrayList<>();

    public User(String name, LocalDate dob, String username, String email, String password) {
        this.name = name;
        this.dob = dob;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String name, LocalDate dob, String username, String email, String password, Collection<Role> roles) {
        this.name = name;
        this.dob = dob;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void addTask(Task task) {
        if (task != null) {
            tasks.add(task);
        }
    }

    public void addRole(Role role) {
        if (role != null) {
            roles.add(role);
        }
    }
}