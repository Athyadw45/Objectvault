/* (C) 2024 */
package com.objectvault.objectvault.controller;

import com.objectvault.objectvault.dto.ListFilesResponseDTO;
import com.objectvault.objectvault.dto.MinioUploadDTO;
import com.objectvault.objectvault.entity.UserEntity;
import com.objectvault.objectvault.services.Impl.MinioService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ObjectStoreController {
  private final MinioService minioService;

  @PostMapping(path = "/v1/upload-file")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    String userId = getUser();
    MinioUploadDTO upload_status = minioService.uploadFile(userId, file);
    if (!upload_status.success()) {
      return ResponseEntity.internalServerError().body(upload_status.message());
    }
    return ResponseEntity.ok(upload_status.message());
  }

  @GetMapping(
      path = "/v1/list-files",
      headers = "accept=" + MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ListFilesResponseDTO> listFiles() {
    String userId = getUser();
    ListFilesResponseDTO listFilesResponseDTO = minioService.listFiles(userId);

    if (listFilesResponseDTO != null) {
      return ResponseEntity.ok(listFilesResponseDTO);
    }
    return ResponseEntity.internalServerError().body(ListFilesResponseDTO.builder().build());
  }

  @DeleteMapping(path = "/v1/deleteobject/{filename}")
  public ResponseEntity<String> deleteObject(@PathVariable("filename") String filename) {
    String userId = getUser();
    Optional<String> deleteStatus = minioService.deleteObject(userId, filename);

    return deleteStatus.isPresent()
        ? ResponseEntity.ok(deleteStatus.get())
        : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to delete file " + filename);
  }

  public String getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserEntity userEntity = (UserEntity) authentication.getPrincipal();
    return userEntity.getUserid();
  }
}
