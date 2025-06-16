package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Resignation;
import com.mycompany.myapp.repository.ResignationRepository;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ResignationService {

    private final ResignationRepository resignationRepository;
    private final UserService userService;

    public ResignationService(ResignationRepository resignationRepository, UserService userService) {
        this.resignationRepository = resignationRepository;
        this.userService = userService;
    }

    public Page<Resignation> findAll(Pageable pageable) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        if (hasAuthority("ROLE_ADMIN") || hasAuthority("ROLE_MANAGER")) {
            return resignationRepository.findAll(pageable);
        }
        return resignationRepository.getResignationlogin(login, pageable);
    }

    public boolean checkRg(LocalDate now) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        long d = resignationRepository.countByStatusAndSubmissionDate(now.getMonthValue(), now.getYear(), login);
        if (d >= 3) return false;
        return true;
    }

    private boolean hasAuthority(String authority) {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority().equals(authority));
    }
}
