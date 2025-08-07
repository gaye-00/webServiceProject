package com.webService.etudiant.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.webService.etudiant.client.GrpcClasseClient;
import com.webService.etudiant.dto.ClasseDto;
import com.webService.etudiant.dto.StudentRequest;
import com.webService.etudiant.dto.StudentResponse;
import com.webService.etudiant.service.StudentService;
import com.webService.grpc.config.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/students")
public class StudentController {

    private final GrpcClasseClient grpcClasseClient;
    private final StudentService studentService;
    private final Logger logger = LoggerFactory.getLogger(StudentController.class);

    // @GetMapping
    // public ResponseEntity<List<StudentResponse>> lister() {
    //     return studentService.getAll();
    // }

    @GetMapping("/classe/rechercher/{id}")
    public ResponseEntity<?> rechercherClasse(@PathVariable String id) {
        try {
            ClasseResponse classeGrpc = grpcClasseClient.rechercherClasseParId(id);
            logger.info("\nClasse trouvée: {} \n", classeGrpc);
            ClasseDto classeDto = new ClasseDto(classeGrpc);
            return ResponseEntity.ok(classeDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trouvée");
        }
    }

    @GetMapping("/classe/lister")
    public ResponseEntity<List<ClasseDto>> listerClasses() {
        try {
            List<ClasseResponse> classesGrpc = grpcClasseClient.lister();
            logger.info("\nClasses trouvées NB: {} \n", classesGrpc.size());
            List<ClasseDto> classesDto = classesGrpc.stream()
                    .map(ClasseDto::new)
                    .toList();
            return ResponseEntity.ok(classesDto);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des classes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<StudentResponse> rechercher(@PathVariable("id") String id) {
    //     return studentService.getById(id);
    // }

    @PatchMapping("/{id}")
    public ResponseEntity<String> modifier(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return studentService.patchUpdate(id, updates);
    }

    @PostMapping
    public ResponseEntity<?> ajouter(@RequestBody @Valid StudentRequest requestItem) {
        // Verifier si la classe existe d'abord
        ClasseResponse classeGrpc = grpcClasseClient.rechercherClasseParId(requestItem.getClasseId());
        if (classeGrpc == null) {
            logger.error("\nClasse non trouvée pour l'ID: {}\n", requestItem.getClasseId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trouvée");
        }
        return studentService.create(requestItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifier(@PathVariable("id") String id, @RequestBody @Valid StudentRequest requestItem) {
        return studentService.update(id, requestItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> supprimer(@PathVariable("id") String id) {
        return studentService.delete(id);
    }
}

