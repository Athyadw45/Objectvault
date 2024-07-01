package com.objectvault.objectvault.dto;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String jwtToken, Long token_expiration, String message) {
}
