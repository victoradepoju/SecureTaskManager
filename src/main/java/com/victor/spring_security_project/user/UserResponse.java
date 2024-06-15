package com.victor.spring_security_project.user;

import lombok.Builder;

@Builder
public record UserResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        Integer noOfTasks
) {
}
