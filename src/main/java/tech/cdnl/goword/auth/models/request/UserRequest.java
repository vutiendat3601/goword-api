package tech.cdnl.goword.auth.models.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserRequest {

    public static interface SignUp {
    }

    public static interface Update {
    }

    @NotBlank(message = "The firstName empty.", groups = { SignUp.class, Update.class })
    private String firstName;

    private String lastName;

    @NotBlank(message = "The email empty.", groups = { SignUp.class, Update.class })
    @Pattern(message = "[] Wrong email format.", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", groups = {
            SignUp.class, Update.class })
    private String email;

    @NotBlank(message = "The password empty.", groups = { SignUp.class })
    @Length(min = 6, max = 32, groups = { SignUp.class })
    @Pattern(message = "[] Wrong password format.", regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+", groups = {
            SignUp.class })
    private String password;
}
