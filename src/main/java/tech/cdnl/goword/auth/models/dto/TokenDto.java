package tech.cdnl.goword.auth.models.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.cdnl.goword.user.models.dto.UserDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenDto {

    private String accessToken;

    private String refreshToken;

    private UUID sessionId;

    @JsonProperty("userInfo")
    private UserDto user;
}
