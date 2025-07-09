package com.webService.etudiant.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webService.etudiant.dto.StudentRequest;
import com.webService.etudiant.dto.StudentResponse;
import com.webService.etudiant.model.DEPARTEMENT;
import com.webService.etudiant.model.LEVEL;
import com.webService.etudiant.model.Student;
import com.webService.etudiant.repository.StudentRepository;

import io.micrometer.common.util.StringUtils;
// import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);


    public ResponseEntity<List<StudentResponse>> getAll() {
        try {
            List<Student> items = new ArrayList<>();
            studentRepository.findAll().forEach(items::add);

            if (items.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<StudentResponse> responseItems = items.stream().map(this::mapToStudentResponse)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(responseItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<StudentResponse> getById(String id) {
        Optional<Student> existingItemOptional = studentRepository.findById(id);

        if (existingItemOptional.isPresent()) {
            return new ResponseEntity<>(mapToStudentResponse(existingItemOptional.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<String> create(StudentRequest studentRequest) {
        try {
            Student student = mapToStudent(studentRequest);
            student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
            
            studentRepository.save(student);

            logger.info("\n\nStudent create successfully.\n");
            return ResponseEntity.ok("Student créée avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la creation =>" + e.getMessage());
        }
    }

    public ResponseEntity<String> update(String id, StudentRequest studentRequest) {
        try {
            Optional<Student> existingItemOptional = studentRepository.findById(id);
            if(existingItemOptional.isPresent()) {
                Student existingItem = existingItemOptional.get();

                if(StringUtils.isNotBlank(studentRequest.getLevel().name())) {
                    existingItem.setLevel(studentRequest.getLevel());
                }

                if(StringUtils.isNotBlank(studentRequest.getDepartment().name())) {
                    existingItem.setDepartment(studentRequest.getDepartment());
                }

                if(StringUtils.isNotBlank(studentRequest.getFirstName())) {
                    existingItem.setFirstName(studentRequest.getFirstName());
                }

                if(StringUtils.isNotBlank(studentRequest.getLastName())) {
                    existingItem.setLastName(studentRequest.getLastName());
                }

                // if(StringUtils.isNotBlank(studentRequest.getPassword())) {
                    // todo change password
                // }


                existingItem.setLastModifiedAt(LocalDateTime.now());

                studentRepository.save(existingItem);

                return ResponseEntity.ok("Student mise à jour avec succès");
            } else {
                return ResponseEntity.status(404)
                        .body("Erreur lors de la mise à jour => Student inexistante");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du Student => " + e.getMessage());
        }
    }

    public ResponseEntity<String> patchUpdate(String id, Map<String, Object> updates) {
        try {
            Optional<Student> existingItemOptional = studentRepository.findById(id);

            if (existingItemOptional.isPresent()) {
                Student existingItem = existingItemOptional.get();

                if(updates.containsKey("level")) {
                    existingItem.setLevel(LEVEL.valueOf((String) updates.get("level")));
                }

                if(updates.containsKey("department")) {
                    existingItem.setDepartment(DEPARTEMENT.valueOf((String) updates.get("department")));
                }

                if(updates.containsKey("firstName")) {
                    existingItem.setFirstName((String) updates.get("firstName"));
                }

                if(updates.containsKey("lastName")) {
                    existingItem.setLastName((String) updates.get("lastName"));
                }

                // if(updates.containsKey("password"))

                existingItem.setLastModifiedAt(LocalDateTime.now());
                studentRepository.save(existingItem);

                return ResponseEntity.ok("Student mise à jour avec succès");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Erreur lors de la mise à jour => Student inexistante");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du Student => " + e.getMessage());
        }
    }

    public ResponseEntity<HttpStatus> delete(String id) {
        try {
            studentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    public StudentResponse mapToStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .level(student.getLevel())
                .department(student.getDepartment())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .active(student.isActive())
                .blocked(student.isBlocked())
                .build();
    }

    public Student mapToStudent(StudentRequest studentRequest) {
        return Student.builder()
                .level(Objects.requireNonNull(studentRequest.getLevel(), "Level cannot be null"))
                .department(Objects.requireNonNull(studentRequest.getDepartment(), "Department cannot be null"))
                .firstName(Objects.requireNonNull(studentRequest.getFirstName(), "First Name cannot be null"))
                .lastName(Objects.requireNonNull(studentRequest.getLastName(), "Last Name cannot be null"))
                .email(Objects.requireNonNull(studentRequest.getEmail(), "Email cannot be null"))
                .creatAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();
    }
}

