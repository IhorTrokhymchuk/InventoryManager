package project.inventorymanager.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.SpaceUsage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.dropbox.provider.DropboxClientProvider;
import project.inventorymanager.exception.dropbox.DropboxApiException;
import project.inventorymanager.exception.file.WorkWithFileExceptions;

@Component
@RequiredArgsConstructor
public class DropboxUtil {
    private static final String SLASH = "/";
    private final DropboxClientProvider dropboxClientProvider;

    public String uploadFile(String filePath) {
        DbxClientV2 client = dropboxClientProvider.getClient();
        File file = new File(filePath);
        long fileSize = file.length();

        try {
            checkFreeSpace(fileSize);
            try (InputStream in = new FileInputStream(file)) {
                FileMetadata metadata = client.files()
                        .uploadBuilder(SLASH + getFileName(filePath))
                        .uploadAndFinish(in);
                return metadata.getId();
            }
        } catch (IOException | DbxException e) {
            throw new WorkWithFileExceptions("Error uploading file: " + e.getMessage());
        }
    }

    private void checkFreeSpace(long fileSize) throws DbxException {
        long freeSpace = getFreeSpace();
        if (fileSize > freeSpace) {
            throw new WorkWithFileExceptions("Not enough space on Dropbox to upload the file.");
        }
    }

    private long getFreeSpace() throws DbxException {
        DbxClientV2 client = dropboxClientProvider.getClient();
        SpaceUsage spaceUsage = client.users().getSpaceUsage();
        long usedSpace = spaceUsage.getUsed();
        long allocatedSpace = spaceUsage.getAllocation().getIndividualValue().getAllocated();
        return allocatedSpace - usedSpace;
    }

    private String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf(SLASH) + 1);
    }

    public String getDownloadUrl(String dropboxId) {
        DbxClientV2 client = dropboxClientProvider.getClient();
        try {
            Metadata metadata = client.files().getMetadata(dropboxId);
            return client.files().getTemporaryLink(metadata.getPathLower()).getLink();
        } catch (DbxException e) {
            throw new DropboxApiException("Cant get download url with dropbox file id: "
                    + dropboxId + ", " + e.getMessage());
        }
    }

    public void deleteFile(String dropboxId) {
        try {
            dropboxClientProvider.getClient().files().deleteV2(dropboxId);
        } catch (DbxException e) {
            throw new DropboxApiException("Error deleting file: " + e.getMessage());
        }
    }
}
