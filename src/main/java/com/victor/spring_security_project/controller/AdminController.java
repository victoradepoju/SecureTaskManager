package com.victor.spring_security_project.controller;

import com.victor.spring_security_project.common.PageResponse;
import com.victor.spring_security_project.task.TaskService;
import com.victor.spring_security_project.user.UserResponse;
import com.victor.spring_security_project.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final TaskService taskService;
    private final UserService userService;

    @DeleteMapping("/task/delete/{task-id}")
    public ResponseEntity<Integer> deleteTask (
            @PathVariable("task-id") Integer taskId,
            Authentication loggedInUser
    ) {
        return ResponseEntity.ok(taskService.deleteTask(taskId, loggedInUser));
    }

    @GetMapping("/view")
    public ResponseEntity<String> adminView(
            Authentication connectedUser
    ) {
        String responseMessage = "You are seeing this because you are ADMIN authenticated " +
                "with username::" + connectedUser.getName();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserResponse>> findAllUsers(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication loggedInUser
    ) {

        return ResponseEntity.ok(userService.findAllUsers(page, size, loggedInUser));
    }
}
