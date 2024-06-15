package com.victor.spring_security_project.task;

public record TaskEditRequest(
        String title,

        String description
) {
}
