package com.victor.spring_security_project.controller;

import com.victor.spring_security_project.common.PageResponse;
import com.victor.spring_security_project.task.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Integer> create(
            @RequestBody @Valid TaskRequest taskRequest,
            Authentication loggedInUser
    ) {
        return ResponseEntity.ok(taskService.create(taskRequest, loggedInUser));
    }

    @GetMapping("{task-id}")
    public ResponseEntity<TaskResponse> findTaskById(
            @PathVariable("task-id") Integer taskId
    ) {
        return ResponseEntity.ok(taskService.findById(taskId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<TaskResponse>> findAllTasks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(taskService.findAllTasks(page, size));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<TaskResponse>> findAllTasksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication loggedInUser
    ) {
        return ResponseEntity.ok(taskService.findAllTasksByOwner(page, size, loggedInUser));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> editTask(
            @PathVariable(name = "taskId", required = true) Integer taskId,
            @RequestBody TaskEditRequest taskEditRequest,
            Authentication loggedInUser
    ) {
        return ResponseEntity.ok(taskService.editTask(taskId, taskEditRequest, loggedInUser));
    }

    @PatchMapping("/status/{task-id}")
    public ResponseEntity<Integer> updateTaskStatus(
            @PathVariable("task-id") Integer taskId,
            Authentication loggedInUser
    ) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, loggedInUser));
    }

    @DeleteMapping("/delete/{task-id}")
    public ResponseEntity<Integer> deleteTask (
            @PathVariable("task-id") Integer taskId,
            Authentication loggedInUser
    ) {
        return ResponseEntity.ok(taskService.deleteTask(taskId, loggedInUser));
    }
}
