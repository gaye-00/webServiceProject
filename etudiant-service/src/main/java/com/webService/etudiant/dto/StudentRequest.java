package com.webService.etudiant.dto;

import com.webService.etudiant.model.DEPARTEMENT;
import com.webService.etudiant.model.LEVEL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentRequest {

    // private String INE;

    @NotNull(message = "Level is required")
    // @NotBlank(message = "Level is not a valid")
    private LEVEL level;

    @NotNull(message = "Department is required")
    // @NotBlank(message = "Department is not a valid")
    private DEPARTEMENT department;

    @NotNull(message = "First name is required")
    @NotBlank(message = "First name is not a valid")
    private String firstName;

    @NotNull(message = "Last name is required")
    @NotBlank(message = "Last name is not a valid")
    private String lastName;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is not a valid")
    private String password;

    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is not a valid")
    @Email(message = "Email is not a valid format")
    private String email;

    @NotNull(message = "Classe is required")
    @NotBlank(message = "Classe is not a valid")
    private String classeId;

}
