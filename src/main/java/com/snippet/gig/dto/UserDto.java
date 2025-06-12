package com.snippet.gig.dto;

import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Role;
import com.snippet.gig.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    // both args and no arg constructor is necessary
    private Long id;
    private String name;
    private String dob;
    private String username;
    private String email;
    private Collection<Role> roles;
}
