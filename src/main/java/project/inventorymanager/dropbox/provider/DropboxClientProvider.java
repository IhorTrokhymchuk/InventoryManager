package project.inventorymanager.dropbox.provider;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DropboxClientProvider {
    private static final DbxRequestConfig requestConfig
            = new DbxRequestConfig("authorize-inventory-manager");
    private final DropboxTokenProvider dropboxTokenProvider;
    private DbxClientV2 client;

    public DbxClientV2 getClient() {
        if (client == null || isClientExpired()) {
            createNewClient();
        }
        return client;
    }

    private boolean isClientExpired() {
        try {
            client.users().getCurrentAccount();
            return false;
        } catch (DbxException e) {
            return true;
        }
    }

    private void createNewClient() {
        String accessToken = dropboxTokenProvider.getAccessToken();
        this.client = new DbxClientV2(requestConfig, accessToken);
    }
}
