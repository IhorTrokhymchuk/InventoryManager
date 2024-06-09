package project.inventorymanager.dropbox.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import project.inventorymanager.dto.dropbox.RefreshResponseDto;
import project.inventorymanager.exception.dropbox.DropboxApiException;

@Component
@RequiredArgsConstructor
public class DropboxTokenProvider {
    private static final String EXCHANGE_URL = "https://api.dropboxapi.com/oauth2/token";
    private final MultiValueMap<String, String> dropboxParams;

    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response
                    = restTemplate.postForEntity(EXCHANGE_URL, dropboxParams, String.class);
            RefreshResponseDto refreshResponseDto = getRefreshResponseDto(response.getBody());
            return refreshResponseDto.getAccessToken();
        } catch (HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
            throw new DropboxApiException(
                    "Cant get access token with refresh token: " + e.getMessage());
        }
    }

    private RefreshResponseDto getRefreshResponseDto(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(body, RefreshResponseDto.class);
    }
}
