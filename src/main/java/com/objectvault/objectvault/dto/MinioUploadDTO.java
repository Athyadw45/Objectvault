package com.objectvault.objectvault.dto;

import lombok.Builder;

@Builder
public record MinioUploadDTO(boolean success,String message) {
}
