package com.objectvault.objectvault.controller;

import com.objectvault.objectvault.services.Impl.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ObjectStoreController {
    private final MinioService minioService;

    @PostMapping(
            path= "/v1/upload-file",
            headers="accept="+ MediaType.APPLICATION_JSON_VALUE,
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> uploadFile(){
        minioService.uploadFile();
        return ResponseEntity.ok("File uploaded successfully");
    }
}
