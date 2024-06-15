package com.victor.spring_security_project.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users")
    public ResponseEntity<String> getUser(
            Authentication connectedUser
    ) {
        String responseMessage = "You are seeing this because you are authenticated " +
                "with username::" + connectedUser.getName() + " with authorities::" +
                connectedUser.getAuthorities() + " with principal::"
                + connectedUser.getPrincipal();

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    @GetMapping("user/view")
    public ResponseEntity<String> userView(
            Authentication connectedUser
    ) {
        String responseMessage = "You are seeing this because you are USER authenticated " +
                "with username::" + connectedUser.getName();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
