package com.objectvault.objectvault.controller;

import com.objectvault.objectvault.dto.ListFilesResponseDTO;
import com.objectvault.objectvault.entity.UserEntity;
import com.objectvault.objectvault.services.Impl.MinioService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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

    @DeleteMapping(
            path= "/v1/deleteobject/{filename}"
    )
    public ResponseEntity<String> deleteObject(@PathVariable ("filename") String filename){
        String userId= getUser();
       Optional<String> deleteStatus =  minioService.deleteObject(userId,filename);
//        return (deleteStatus.isPresent()?){
//           return ResponseEntity.ok(deleteStatus.get())
        return deleteStatus.isPresent()
                ?ResponseEntity.ok(deleteStatus.get())
                :ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to delete file "+filename);
    }



    public String getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        return userEntity.getUserid();
    }
}
