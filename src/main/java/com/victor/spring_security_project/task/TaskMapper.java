package com.victor.spring_security_project.task;

import org.springframework.stereotype.Service;

@Service
public class TaskMapper {
    public Task toTask(TaskRequest taskRequest) {
        return Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(false)
                .build();
    }

    public TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .owner(task.getOwner().getFirstName())
                .status(task.isStatus())
                .createdAt(task.getCreatedAt())
                .modifiedAt(task.getModifiedAt())
                .build();
    }
}
