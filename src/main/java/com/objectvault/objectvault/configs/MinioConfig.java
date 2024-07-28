package com.objectvault.objectvault.configs;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class MinioConfig {
    @Value("${minio.client.username}")
    private String minio_username;

    @Value("${minio.client.password}")
    private String minio_password;

    @Value("${minio.server.endpoint}")
    private String minio_endpoint;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minio_endpoint,9000,false)
                .credentials(minio_username,minio_password)
                .build();
    }


}
