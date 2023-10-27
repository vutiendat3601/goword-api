package tech.cdnl.goword.auth.models.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.cdnl.goword.user.models.dto.UserDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionDto {
    private UUID id;

    private ZonedDateTime signedInAt;

    private ZonedDateTime signedOutAt;

    private String refreshToken;

    private UserDto user;
}
