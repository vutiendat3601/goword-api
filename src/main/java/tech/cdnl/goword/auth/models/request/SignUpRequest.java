package tech.cdnl.goword.auth.models.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "The firstName empty.")
    private String firstName;

    private String lastName;

    @NotBlank(message = "The email empty.")
    @Pattern(message = "[] Wrong email format.", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank(message = "The password empty.")
    @Length(min = 6, max = 32)
    @Pattern(message = "[] Wrong password format.", regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+")
    private String password;
}
