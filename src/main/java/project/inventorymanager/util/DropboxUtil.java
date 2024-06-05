package project.inventorymanager.util;

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
import project.inventorymanager.exception.file.WorkWithFileExceptions;

@Component
@RequiredArgsConstructor
public class DropboxUtil {
    private static final String SLASH = "/";
    private final DbxClientV2 dbxClientV2;

    public String uploadFile(String filePath) {
        File file = new File(filePath);
        long fileSize = file.length();

        try {
            checkFreeSpace(fileSize);
            try (InputStream in = new FileInputStream(file)) {
                FileMetadata metadata = dbxClientV2.files().uploadBuilder(SLASH + getFileName(filePath))
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
        SpaceUsage spaceUsage = dbxClientV2.users().getSpaceUsage();
        long usedSpace = spaceUsage.getUsed();
        long allocatedSpace = spaceUsage.getAllocation().getIndividualValue().getAllocated();
        return allocatedSpace - usedSpace;
    }

    private String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf(SLASH) + 1);
    }

    public String getDownloadUrl(String dropboxId) {
        try {
            Metadata metadata = dbxClientV2.files().getMetadata(dropboxId);
            return dbxClientV2.files().getTemporaryLink(metadata.getPathLower()).getLink();
        } catch (DbxException e) {
            throw new WorkWithFileExceptions("Cant get download url with dropbox file id: "
                    + dropboxId + ", " + e.getMessage());
        }
    }
}
