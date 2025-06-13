package com.snippet.gig.data;

import com.snippet.gig.entity.Role;
import com.snippet.gig.entity.User;
import com.snippet.gig.enums.Roles;
import com.snippet.gig.repository.RoleRepository;
import com.snippet.gig.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles =  Set.of("ADMIN", "USER", "MANAGER");
//        createDefaultRoleIfNotExits(defaultRoles);
//        createDefaultAdminIfNotExits();
//        createUsers();
//        createManagers();
    }

    private void createUsers() {
        Role userRole = roleRepository.findByName("USER").get();
        ArrayList<User> users = new ArrayList<>();
        User user1 = new User("tester1", LocalDate.now(), "tester1", "tester1@test.com", passwordEncoder.encode("tester1@test"));
        User user2 = new User("tester2", LocalDate.now(), "tester2", "tester2@test.com", passwordEncoder.encode("tester2@test"));
        User user3 = new User("tester3", LocalDate.now(), "tester3", "tester3@test.com", passwordEncoder.encode("tester3@test"));
        User user4 = new User("tester4", LocalDate.now(), "tester4", "tester4@test.com", passwordEncoder.encode("tester4@test"));
        User user5 = new User("tester5", LocalDate.now(), "tester5", "tester5@test.com", passwordEncoder.encode("tester5@test"));

        user1.addRole(userRole);
        user2.addRole(userRole);
        user3.addRole(userRole);
        user4.addRole(userRole);
        user5.addRole(userRole);

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);

        userRepository.saveAll(users);
    }

    private void createManagers() {
        Role managerRole = roleRepository.findByName("MANAGER").get();
        Role userRole = roleRepository.findByName("USER").get();

        ArrayList<User> managers = new ArrayList<>();
        User manager1 = new User("manager1", LocalDate.now(), "manager1", "manager1@test.com", passwordEncoder.encode("manager1@test"));
        User manager2 = new User("manager2", LocalDate.now(), "manager2", "manager2@test.com", passwordEncoder.encode("manager2@test"));
        User manager3 = new User("manager3", LocalDate.now(), "manager3", "manager3@test.com", passwordEncoder.encode("manager3@test"));

        manager1.addRole(managerRole);
        manager1.addRole(userRole);

        manager2.addRole(managerRole);
        manager3.addRole(managerRole);

        managers.add(manager1);
        managers.add(manager2);
        managers.add(manager3);

        userRepository.saveAll(managers);
    }

    private void createDefaultAdminIfNotExits() {
        Role adminRole = roleRepository.findByName("ADMIN").get();
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setName("admin" + i);
            user.setUsername("admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " created successfully.");
        }
    }
    
    private void createDefaultRoleIfNotExits(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role:: new).forEach(roleRepository::save);
    }
}