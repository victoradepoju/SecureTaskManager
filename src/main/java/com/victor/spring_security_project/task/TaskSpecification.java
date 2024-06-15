package com.victor.spring_security_project.task;

import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("owner").get("id"), ownerId);
    }
}
