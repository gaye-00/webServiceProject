package com.webService.classe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.webService.grpc.config.ClasseRequest;

import com.github.javafaker.Faker;
import com.webService.classe.model.CYCLE;
import com.webService.classe.model.FILIERE;
import com.webService.classe.service.ClasseService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class initializerClasse implements CommandLineRunner {

    private final ClasseService classeService;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            ClasseRequest request = ClasseRequest.newBuilder()
                .setName(faker.educator().course())
                .setDescription(faker.lorem().sentence())
                .setCycle(CYCLE.LICENCE.name())
                .setFiliere(FILIERE.INFORMATIQUE.name())
                .build();

            classeService.create(request);
        }
    }
    
}
