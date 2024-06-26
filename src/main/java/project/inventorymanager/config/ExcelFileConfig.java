package project.inventorymanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelFileConfig {
    @Value("${excel.file.path}")
    private String directoryPath;

    @Bean
    public String directoryPath() {
        return directoryPath;
    }
}
