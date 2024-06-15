package com.victor.spring_security_project.user;

import com.victor.spring_security_project.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public PageResponse<UserResponse> findAllUsers(
            int page,
            int size,
            Authentication loggedInUser
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending());

        Page<User> usersPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = usersPage.stream()
                .map(userMapper::toUserResponse)
                .toList();

        return new PageResponse<>(
                userResponses,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.isFirst(),
                usersPage.isLast()
        );
    }
}
