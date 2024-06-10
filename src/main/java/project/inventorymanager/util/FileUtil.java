package project.inventorymanager.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static boolean clearDirectory(String directoryPath) {
        Path directory = Paths.get(directoryPath);
        if (Files.exists(directory) && Files.isDirectory(directory)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
                for (Path file : directoryStream) {
                    if (Files.isRegularFile(file)) {
                        Files.delete(file);
                    }
                }
                return true;
            } catch (IOException e) {
                throw new WorkWithFileExceptions(
                        "Unable to delete files in directory with path: " + directoryPath);
            }
        } else {
            throw new WorkWithFileExceptions("Directory with path '" + directoryPath
                    + "' does not exist or is not a directory");
        }
    }
}
