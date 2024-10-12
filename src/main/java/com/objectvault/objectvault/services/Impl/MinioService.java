package com.objectvault.objectvault.services.Impl;

import com.objectvault.objectvault.dto.ListFilesDTO;
import com.objectvault.objectvault.dto.ListFilesResponseDTO;
import com.objectvault.objectvault.dto.MinioUploadDTO;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioClient minioClient;
    @Value("${minio.server.endpoint}")
    private String minio_endpoint;

    public MinioUploadDTO uploadFile(String user, MultipartFile multipartFile){
        try{
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(user).build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(user).build());
            }
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(user)
                    .object(multipartFile.getOriginalFilename())
                    .contentType(multipartFile.getContentType())
                    .stream(multipartFile.getInputStream(),multipartFile.getSize(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);

            return MinioUploadDTO.builder()
                    .success(true)
                    .message("Successfully uploaded "+multipartFile.getOriginalFilename())
                    .build();

        }catch (Exception e){
            log.error("Error uploading File",e);
            return MinioUploadDTO.builder()
                    .success(false)
                    .message("File upload failed "+e.getMessage())
                    .build();
        }
    }
    public ListFilesResponseDTO listFiles(String user){
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(user)
                            .maxKeys(100)
                            .build());
            List<ListFilesDTO> objectlist = new ArrayList<>();

            for (Result<Item> result : results) {
                    Item item = result.get();

                ListFilesDTO listFilesDTO = ListFilesDTO.builder()
                        .name(item.objectName())
                        .isDir(item.isDir())
                        .size(String.valueOf(item.size()))
                        .lastModified(String.valueOf(item.lastModified()))
                        .url(getFinalUrl(item,user))
                        .build();

                objectlist.add(listFilesDTO);
            }
            return ListFilesResponseDTO.builder()
                    .objectList(objectlist)
                    .build();


        }catch (Exception e){
            log.error("Cannot retrive files from"+user,e);

        }
        return ListFilesResponseDTO.builder()
                .objectList(List.of())
                .build();
    }
    public Optional<String> deleteObject(String user , String object){
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(user).object(object).build());
            return Optional.of(object +" deleted successfully");
        }catch (Exception e){
            log.error("Exception while deleting object "+object,e);
            return Optional.empty();
        }

    }

    private String getFinalUrl(Item item, String user) throws Exception{
        Map<String, String> reqParams = new HashMap<String, String>();
        String contentDisposition = URLEncoder.encode("attachment; filename=\"%s\"".formatted(item.objectName()), StandardCharsets.UTF_8);
        reqParams.put("response-content-disposition",contentDisposition);
        String url =
                minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(user)
                                .object(item.objectName())
                                .expiry(2, TimeUnit.HOURS)
                                .extraQueryParams(reqParams)
                                .build());

       return "http://" + minio_endpoint + url.substring(StringUtils.ordinalIndexOf(url,":", 2));

    }


}
