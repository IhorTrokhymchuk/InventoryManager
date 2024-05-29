package project.inventorymanager.util;

import java.io.File;
import org.springframework.stereotype.Component;
import project.inventorymanager.exception.file.WorkWithFileExceptions;

@Component
public class FileUtil {
    public static boolean deleteFile(String filePath) {
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            return fileToDelete.delete();
        }
        throw new WorkWithFileExceptions("File with path '" + filePath + "' is not exist");
    }
}
