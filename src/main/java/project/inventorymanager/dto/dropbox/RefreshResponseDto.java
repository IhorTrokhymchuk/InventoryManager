package project.inventorymanager.dto.dropbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RefreshResponseDto {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Long expiresIn;
}
