/* (C) 2024 */
package com.objectvault.objectvault.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;

@Builder
public record ListFilesResponseDTO(List<ListFilesDTO> objectList) implements Serializable {}
