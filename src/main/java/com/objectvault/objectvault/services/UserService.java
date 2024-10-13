/* (C) 2024 */
package com.objectvault.objectvault.services;

import com.objectvault.objectvault.dto.LoginUserDTO;
import com.objectvault.objectvault.dto.RegisterUserDTO;
import com.objectvault.objectvault.entity.UserEntity;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

  public Optional<UserEntity> getUser(String id);

  public String register(RegisterUserDTO registerUserDTO);

  public Optional<UserDetails> loginUser(LoginUserDTO loginUserDTO);
}
