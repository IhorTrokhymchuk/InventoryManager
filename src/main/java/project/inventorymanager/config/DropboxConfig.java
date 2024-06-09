package project.inventorymanager.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Configuration
@RequiredArgsConstructor
public class DropboxConfig {
    @Value("${dropbox.app.key}")
    private final String appKey;
    @Value("${dropbox.app.secret}")
    private final String appSecret;
    @Value("${dropbox.app.refresh_token}")
    private final String refreshToken;

    @Bean
    public MultiValueMap<String, String> dropboxParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);
        params.add("client_id", appKey);
        params.add("client_secret", appSecret);
        return params;
    }
}
