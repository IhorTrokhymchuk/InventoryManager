package project.inventorymanager.schedulingtask;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.inventorymanager.exception.file.WorkWithFileExceptions;

@Component
@RequiredArgsConstructor
public class FileTask {
    private final String filePath;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void ifBookingUnconfirmedOneHour() {
        System.out.println("run delete");
        try {
            Path directory = Paths.get(filePath);
            if (Files.exists(directory) && Files.isDirectory(directory)) {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
                    for (Path file : directoryStream) {
                        if (Files.isRegularFile(file)) {
                            Files.delete(file);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new WorkWithFileExceptions("Problem with delete files with path: " + filePath);
        }
    }
}
