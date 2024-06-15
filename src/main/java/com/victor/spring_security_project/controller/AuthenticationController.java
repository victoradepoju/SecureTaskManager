package com.victor.spring_security_project.controller;

import com.victor.spring_security_project.authentication.AuthenticationService;
import com.victor.spring_security_project.authentication.LoginRequest;
import com.victor.spring_security_project.authentication.RegisterRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(
            @RequestBody @Valid RegisterRequest registerRequest
    ) throws MessagingException {
        log.info("User Registration initialized");

        authenticationService.registerUser(registerRequest);
        return ResponseEntity.accepted().body("An email has been seen to " +
                "activate you account");
    }

    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(
            @RequestBody @Valid RegisterRequest registerRequest
    ){
        log.info("Admin Registration initialized");

        authenticationService.registerAdmin(registerRequest);
        return ResponseEntity.accepted().body("You have been registered");
    }

    @GetMapping("/activate-account")
    public void activateAccount(
            @RequestParam String token
    ) throws MessagingException
    {
        authenticationService.activateAccount(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest
    ) {
        String loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
