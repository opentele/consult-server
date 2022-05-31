package org.opentele.consult.framework;

import org.opentele.consult.domain.security.User;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
            user = userService.getAppUser();
        } else {
            user = userService.getUser(userSession.getUserId());
        }
        return user == null ? Optional.empty() : Optional.of(user);
    }
}