package com.victor.spring_security_project.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class CheckIdFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String victorHeader = request.getHeader("Victor-Header");

        if(!victorHeader.equals("a")) {
            filterChain.doFilter(request, response);
            return;
        }

        throw new AccessDeniedException("You are not a Victor");
    }
}
