package tech.cdnl.goword.user.models.dto;

import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private Set<String> roles;

    private Set<String> permissions;

    public UserDto(UUID id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
