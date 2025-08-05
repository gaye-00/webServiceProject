package com.webService.etudiant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.webService.etudiant.dto.StudentRequest;
import com.webService.etudiant.model.DEPARTEMENT;
import com.webService.etudiant.model.LEVEL;
import com.webService.etudiant.service.StudentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class initializerEtudiant implements CommandLineRunner {

    private final StudentService studentService;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            studentService.create(new StudentRequest(
                LEVEL.LICENCE1,
                DEPARTEMENT.INFORMATIQUE,
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().password(),
                faker.internet().emailAddress(),
                faker.number().digits(5)
            ));
        }
    }
    
}
