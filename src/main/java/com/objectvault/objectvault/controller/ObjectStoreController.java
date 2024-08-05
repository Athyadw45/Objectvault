package com.objectvault.objectvault.controller;

import com.objectvault.objectvault.dto.ListFilesResponseDTO;
import com.objectvault.objectvault.entity.UserEntity;
import com.objectvault.objectvault.services.Impl.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ObjectStoreController {
    private final MinioService minioService;

    @PostMapping(
            path= "/v1/upload-file"
    )
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        String userId= getUser();
        minioService.uploadFile(userId,file);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping(
            path= "/v1/list-files",
            headers="accept="+ MediaType.APPLICATION_JSON_VALUE,
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ListFilesResponseDTO> listFiles(){
        String userId= getUser();
        ListFilesResponseDTO listFilesResponseDTO=minioService.listFiles(userId);

        if (listFilesResponseDTO!=null){
            return ResponseEntity.ok(listFilesResponseDTO);
        }
        return ResponseEntity.internalServerError().body(ListFilesResponseDTO.builder().build());

    }



    public String getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        return userEntity.getUserid();
    }
}
