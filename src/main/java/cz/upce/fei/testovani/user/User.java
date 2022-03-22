package cz.upce.fei.testovani.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String username;
    @NotNull
    private String displayname;

    // Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character:
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    private String password;
}
