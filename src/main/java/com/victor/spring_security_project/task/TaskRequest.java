package com.victor.spring_security_project.task;

import com.victor.spring_security_project.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TaskRequest (

        @NotEmpty(message = "Title is needed for new task") // from validation package
        @NotBlank(message = "Title is needed for new task")
        String title,

        @Size(min = 3, message = "Description should be 3 characters or more")
        @NotEmpty(message = "Description is needed for new registration")
        @NotBlank(message = "Description is needed for new registration")
        String description

        //todo: remove this
//        User owner
){
}
