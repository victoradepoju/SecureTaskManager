package com.victor.spring_security_project.task;

import com.victor.spring_security_project.common.PageResponse;
import com.victor.spring_security_project.exception.OperationNotPermittedException;
import com.victor.spring_security_project.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public Integer create(TaskRequest taskRequest, Authentication loggedInUser) {

        User user = (User) loggedInUser.getPrincipal();

        Task task = taskMapper.toTask(taskRequest);

        task.setOwner(user);

        return taskRepository.save(task).getId();
    }

    public TaskResponse findById(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new EntityNotFoundException("Task not found"));

        return taskMapper.toTaskResponse(task);
    }

    public PageResponse<TaskResponse> findAllTasks(
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Task> tasksPage = taskRepository.findAll(pageable);

        List<TaskResponse> taskResponseList = tasksPage.stream()
                .map(taskMapper::toTaskResponse)
                .toList();

        return new PageResponse<>(
                taskResponseList,
                tasksPage.getNumber(),
                tasksPage.getSize(),
                tasksPage.getTotalElements(),
                tasksPage.getTotalPages(),
                tasksPage.isFirst(),
                tasksPage.isLast()
        );
    }

    public PageResponse<TaskResponse> findAllTasksByOwner(
            int page,
            int size,
            Authentication loggedInUser
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt")
        );

        var userId = ((User) loggedInUser.getPrincipal()).getId();

        Page<Task> tasksPage = taskRepository
                .findAll(TaskSpecification.withOwnerId(userId), pageable);

        List<TaskResponse> taskResponseList = tasksPage.stream()
                .map(taskMapper::toTaskResponse)
                .toList();

        return new PageResponse<>(
                taskResponseList,
                tasksPage.getNumber(),
                tasksPage.getSize(),
                tasksPage.getTotalElements(),
                tasksPage.getTotalPages(),
                tasksPage.isFirst(),
                tasksPage.isLast()
        );
    }

    public Integer updateTaskStatus(Integer taskId, Authentication loggedInUser) {
        User user = (User) loggedInUser.getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if (!Objects.equals(user.getId(), task.getOwner().getId())) {
            throw new OperationNotPermittedException(
                    "You can only update the status of your own task"
            );
        }

        task.setStatus(!task.isStatus());

        taskRepository.save(task);

        return taskId;
    }

    public Integer deleteTask(Integer taskId, Authentication loggedInUser) {
        User user = (User) loggedInUser.getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if (!Objects.equals(user.getId(), task.getOwner().getId())
                && !Objects.equals(user.getRole().getName(), "ADMIN")) {
            throw new OperationNotPermittedException(
                    "You can only delete your own task or you must be an admin"
            );
        }

        taskRepository.delete(task);

        return taskId;
    }

    public TaskResponse editTask(
            Integer taskId,
            TaskEditRequest taskEditRequest,
            Authentication loggedInUser
    ) {
        User user = (User) loggedInUser.getPrincipal();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if (!Objects.equals(user.getId(), task.getOwner().getId())) {
            throw new OperationNotPermittedException(
                    "You can only edit your own task"
            );
        }

        task.setTitle(taskEditRequest.title());
        task.setDescription(taskEditRequest.description());

        taskRepository.save(task);

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