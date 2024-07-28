package com.objectvault.objectvault.services.Impl;

import com.objectvault.objectvault.dto.ListFilesDTO;
import com.objectvault.objectvault.dto.ListFilesResponseDTO;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioClient minioClient;

    public void uploadFile(String user){
        try{
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(user).build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(user).build());
            }
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(user)
                            .object("asiaphotos-2015.zip")
                            .filename("/home/superblazer/Downloads/qr-code.png")
                            .build());
        }catch (Exception e){
            log.error("Error uploading File",e);

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
                        .build();

                objectlist.add(listFilesDTO);
            }
            return ListFilesResponseDTO.builder()
                    .objectList(objectlist)
                    .build();


        }catch (Exception e){
            log.error("Cannot retrive files from"+user,e);

        }
        return null;
    }
}
