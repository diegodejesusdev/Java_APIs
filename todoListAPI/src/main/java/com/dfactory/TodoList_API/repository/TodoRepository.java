package com.dfactory.TodoList_API.repository;

import com.dfactory.TodoList_API.model.TodoEntity;
import com.dfactory.TodoList_API.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    Page<TodoEntity> findByUser(UserEntity user, Pageable pageable);
}
