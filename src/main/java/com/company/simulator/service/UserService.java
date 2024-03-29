package com.company.simulator.service;

import com.company.simulator.model.Role;
import com.company.simulator.model.Team;
import com.company.simulator.model.User;
import com.company.simulator.repos.StudentRepo;
import com.company.simulator.repos.UserRepo;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepo studentRepo;

    @Value("${url.activation.path}")
    private String urlForActivation;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public boolean addUser(User user, String role) {
        final User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        if (role.equalsIgnoreCase("student")) {
            user.setRoles(Collections.singleton(Role.USER));
        }
        if (role.equalsIgnoreCase(Role.TEACHER.name())) {
            user.setRoles(Set.of(Role.USER, Role.TEACHER));
        }
        user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        sendMessage(user);
        return true;
    }

    public boolean activateUserAndAddToCommonTeam(String code) {
        final User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);
        studentRepo.addUserToTeam(user.getId(), Team.COMMON_TEAM);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        final Set<String> roles = Arrays.stream(Role.values())
            .map(Role::name)
            .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        final boolean isEmailChanged = !Objects.equals(email, user.getEmail());
        if (isEmailChanged) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    private void sendMessage(final User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            final String message = String.format(
                "Hello, %s!\nWelcome to SQL-simulator. Please, follow the link: %s%s",
                user.getUsername(),
                urlForActivation,
                user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }
}
