package com.webService.classe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document("students")
public class Classe extends BaseEntity {
    
    private String name;
    private String description;
    private CYCLE cycle;
    private FILIERE filiere;
}
