package com.webService.etudiant.dto;

import com.webService.grpc.config.ClasseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClasseDto {
    private String id;
    private String name;
    private String description;
    private String cycle;
    private String filiere;

    // Constructeur
    public ClasseDto(ClasseResponse response) {
        this.id = response.getId();
        this.name = response.getName();
        this.description = response.getDescription();
        this.cycle = response.getCycle();
        this.filiere = response.getFiliere();
    }
}

