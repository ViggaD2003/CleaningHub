package com.fpu.exe.cleaninghub.config;

import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Optional;

@Configuration
public class ApplicationAuditing implements AuditorAware<Integer> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        
    if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
        return Optional.empty();
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof User) {
        User userPrincipal = (User) principal;
        return Optional.ofNullable(userPrincipal.getId());
    }
    else if (principal instanceof DefaultOAuth2User) {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) principal;
        String email = oauthUser.getAttribute("email");
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(User::getId);
    }
    return Optional.empty();
    }

    @Bean
    AuditorAware<Integer> auditorProvider() {
        return new ApplicationAuditing();
    }
}
