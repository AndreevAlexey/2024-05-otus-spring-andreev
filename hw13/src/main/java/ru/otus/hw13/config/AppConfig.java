package ru.otus.hw13.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "settings")
public class AppConfig {

    private int chunkSize;

    private int pageSize;

    private String prefixMongoId;

}
