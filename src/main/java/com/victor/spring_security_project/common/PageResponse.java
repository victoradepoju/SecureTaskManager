package com.victor.spring_security_project.common;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse <T> (
        List<T> content, // by converting Pageable to a list
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
){
}
