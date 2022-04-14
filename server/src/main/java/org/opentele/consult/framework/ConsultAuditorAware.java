package org.opentele.consult.framework;

import org.opentele.consult.domain.security.User;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.opentele.consult.domain.security.User.AppUserName;

@Component
public class ConsultAuditorAware implements AuditorAware<User> {
    private final UserSession userSession;
    private final UserService userService;

    @Autowired
    public ConsultAuditorAware(UserSession userSession, UserService userService) {
        this.userSession = userSession;
        this.userService = userService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        User user;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            user = userService.getUser(AppUserName);
        } else {
            user = userSession.getCurrentUser();
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }
}
