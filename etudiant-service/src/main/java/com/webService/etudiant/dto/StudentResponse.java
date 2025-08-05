package com.webService.etudiant.dto;

import com.webService.etudiant.model.DEPARTEMENT;
import com.webService.etudiant.model.LEVEL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentResponse {

    private String id;
    // private String INE;
    private LEVEL level;
    private DEPARTEMENT department;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Boolean active;
    private Boolean blocked;
    private String classeId;
}
