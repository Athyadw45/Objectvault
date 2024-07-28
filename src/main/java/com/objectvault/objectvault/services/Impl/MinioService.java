package com.objectvault.objectvault.services.Impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
