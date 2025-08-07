package com.webService.etudiant.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.webService.etudiant.dto.StudentResponse;
import com.webService.etudiant.service.StudentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentQueryResolver {

    private final StudentService studentService;

    @QueryMapping
    public List<StudentResponse> allStudents() {
        ResponseEntity<List<StudentResponse>> response = studentService.getAll();
        return response.getBody();
    }

    @QueryMapping
    public StudentResponse studentById(@Argument String id) {
        ResponseEntity<StudentResponse> response = studentService.getById(id);
        return response.getBody();
    }
}
