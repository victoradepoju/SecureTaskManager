package com.victor.spring_security_project.task;

import com.victor.spring_security_project.user.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponse(
        Integer id,

        String title,

        String description,

        String owner,

        boolean status,

        LocalDateTime createdAt,

        LocalDateTime modifiedAt
) {
}
