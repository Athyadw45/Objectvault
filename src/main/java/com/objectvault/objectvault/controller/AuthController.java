/* (C) 2024 */
package com.objectvault.objectvault.controller;

import com.objectvault.objectvault.dto.LoginResponseDTO;
import com.objectvault.objectvault.dto.LoginUserDTO;
import com.objectvault.objectvault.dto.RegisterUserDTO;
import com.objectvault.objectvault.entity.UserEntity;
import com.objectvault.objectvault.services.JwtService;
import com.objectvault.objectvault.services.UserService;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final JwtService jwtService;

  @GetMapping(
      path = "/showuser/{id}",
      headers = "accept=" + MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public UserEntity getUsers(@PathVariable @NonNull String id) {
    Optional<UserEntity> user = userService.getUser(id);
    if (user.isEmpty()) {
      UserEntity userEntity = new UserEntity();
      return userEntity;
    }
    return user.get();
  }

  @PostMapping(
      path = "/auth/register",
      headers = "accept=" + MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
    String registerResponse = userService.register(registerUserDTO);

    return "User saved".equals(registerResponse)
        ? ResponseEntity.ok("User saved")
        : ResponseEntity.badRequest().body(registerResponse);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDTO loginUserDTO) {
    Optional<UserDetails> authenticatedUser = userService.loginUser(loginUserDTO);

    if (authenticatedUser.isPresent()) {
      String jwtToken = jwtService.generateToken(authenticatedUser.get());
      LoginResponseDTO loginResponseDTO =
          LoginResponseDTO.builder()
              .jwtToken(jwtToken)
              .token_expiration(jwtService.getExpirationTime())
              .message("login successful!")
              .build();

      return ResponseEntity.ok(loginResponseDTO);

    } else {
      LoginResponseDTO loginResponseDTO =
          LoginResponseDTO.builder().message("Unable to login, check credentials").build();
      return ResponseEntity.badRequest().body(loginResponseDTO);
    }
  }
}
