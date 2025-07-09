package com.webService.etudiant.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

// Pour la sérialisation JSON polymorphe
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Student.class, name = "student")
})
@Document("users")
public abstract class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private String photo;

    @Indexed(unique = true)
    private String email;

    private String password;

    @Builder.Default
    private boolean active = false;

    @Builder.Default
    private boolean isBlocked = false;
}
