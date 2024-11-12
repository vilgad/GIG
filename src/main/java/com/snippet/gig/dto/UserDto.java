package com.snippet.gig.dto;

import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Role;
import com.snippet.gig.entity.Task;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDto {
    // both args and no arg constructor is necessary
    private Long id;
    private String name;
    private String dob;
    private String username;
    private String email;
    private String password;
    private List<Task> tasks;
    private Project project;
    private Collection<Role> roles;

    public UserDto() {
    }

    public UserDto(Long id, String name, String dob, String username, String email, String password, List<Task> tasks, Project project, Collection<Role> roles) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.username = username;
        this.email = email;
        this.password = password;
        this.tasks = tasks;
        this.project = project;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(getId(), userDto.getId()) && Objects.equals(getName(), userDto.getName()) && Objects.equals(getDob(), userDto.getDob()) && Objects.equals(getUsername(), userDto.getUsername()) && Objects.equals(getEmail(), userDto.getEmail()) && Objects.equals(getPassword(), userDto.getPassword()) && Objects.equals(getTasks(), userDto.getTasks()) && Objects.equals(getProject(), userDto.getProject()) && Objects.equals(getRoles(), userDto.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDob(), getUsername(), getEmail(), getPassword(), getTasks(), getProject(), getRoles());
    }
}
