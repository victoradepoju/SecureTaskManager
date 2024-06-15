package com.victor.spring_security_project.config;

import com.victor.spring_security_project.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken)
        {

            return Optional.empty();
        }

        var principal = authentication.getPrincipal();

        if(principal instanceof User) {
            User user = (User) principal;
            return Optional.ofNullable(user.getId());
        } else if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            Integer userId = jwt.getClaim("userId");
            return Optional.ofNullable(userId);
        }
        return Optional.empty();
    }
}

