package com.objectvault.objectvault.services;

import com.objectvault.objectvault.dto.LoginUserDTO;
import com.objectvault.objectvault.dto.RegisterUserDTO;
import com.objectvault.objectvault.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService{

    public Optional<UserEntity> getUser(String id);
    public String register(RegisterUserDTO registerUserDTO);
    public Optional<UserDetails> loginUser(LoginUserDTO loginUserDTO);

}
