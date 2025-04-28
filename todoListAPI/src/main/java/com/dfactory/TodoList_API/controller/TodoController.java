package com.dfactory.TodoList_API.controller;

import com.dfactory.TodoList_API.dto.TodoRequest;
import com.dfactory.TodoList_API.dto.TodoResponse;
import com.dfactory.TodoList_API.model.TodoEntity;
import com.dfactory.TodoList_API.model.UserEntity;
import com.dfactory.TodoList_API.repository.TodoRepository;
import com.dfactory.TodoList_API.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(TodoRequest request){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TodoEntity todo = TodoEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(user)
                .build();

        todoRepository.save(todo);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new TodoResponse(todo.getId(), todo.getTitle(), todo.getDescription())
        );
    }

    @GetMapping
    public ResponseEntity<?> getTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") int limit){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<TodoEntity> todos = todoRepository.findByUser(user, (Pageable) PageRequest.of(page, limit));

        var response = new Object(){
            public final java.util.List<TodoResponse> data = todos.getContent().stream()
                    .map(todo -> new TodoResponse(todo.getId(), todo.getTitle(), todo.getDescription()))
                    .collect(Collectors.toList());
            public final int page = todos.getNumber();
            public final int limit = todos.getSize();
            public final long total = todos.getTotalElements();
        };

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long id, @RequestBody TodoRequest request){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TodoEntity todo = todoRepository.findById(id).orElse(null);

        if(todo == null){
            return ResponseEntity.notFound().build();
        }

        if(!todo.getUser().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todoRepository.save(todo);

        return ResponseEntity.ok(
                new TodoResponse(todo.getId(), todo.getTitle(), todo.getDescription())
        );
    }

    public ResponseEntity<?> deleteTodo(@PathVariable Long id){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TodoEntity todo = todoRepository.findById(id).orElse(null);

        if(todo == null){
            return ResponseEntity.notFound().build();
        }

        if(!todo.getUser().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        todoRepository.delete(todo);

        return ResponseEntity.noContent().build();
    }
 }
