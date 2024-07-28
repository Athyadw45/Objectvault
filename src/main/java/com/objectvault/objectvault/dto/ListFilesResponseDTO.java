package com.objectvault.objectvault.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ListFilesResponseDTO(List<ListFilesDTO> objectList) {
}
