/* (C) 2024 */
package com.objectvault.objectvault.dto;

import lombok.Builder;

@Builder
public record RegisterUserDTO(String firstname, String lastname, String email, String password) {}
