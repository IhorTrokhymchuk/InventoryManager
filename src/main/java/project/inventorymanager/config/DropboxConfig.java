package project.inventorymanager.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DropboxConfig {
    private static final String DROPBOX_URL = "inventory-manage-system/statistic";

    @Value("${dropbox.access.token}")
    private String accessToken;

    private DbxRequestConfig dbxRequestConfig;

    @PostConstruct
    public void init() {
        dbxRequestConfig = DbxRequestConfig.newBuilder(DROPBOX_URL).build();
    }

    @Bean
    public DbxClientV2 dbxClientV2() {
        return new DbxClientV2(dbxRequestConfig, accessToken);
    }
}
