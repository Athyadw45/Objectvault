package com.objectvault.objectvault.dto;

import lombok.Builder;

@Builder
public record ListFilesDTO(String name, boolean isDir, String size, String lastModified) {
}
