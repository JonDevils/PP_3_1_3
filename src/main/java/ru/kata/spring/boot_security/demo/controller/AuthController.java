package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.ErorrsValidator.UserValidator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    @Autowired
    public AuthController( UserDetailsServiceImpl userDetailsService, RegistrationService registrationService, UserValidator userValidator) {
        this.userDetailsService = userDetailsService;
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }



}
