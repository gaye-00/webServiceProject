package com.webService.etudiant.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webService.etudiant.dto.StudentRequest;
import com.webService.etudiant.dto.StudentResponse;
import com.webService.etudiant.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable("id") String id) {
        return studentService.getById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchUpdate(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return studentService.patchUpdate(id, updates);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid StudentRequest requestItem) {
        return studentService.create(requestItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") String id, @RequestBody @Valid StudentRequest requestItem) {
        return studentService.update(id, requestItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") String id) {
        return studentService.delete(id);
    }
}

