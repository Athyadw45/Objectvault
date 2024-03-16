package com.objectvault.objectvault.controller;

import com.objectvault.objectvault.entity.UserEntity;
import com.objectvault.objectvault.repositories.UserRepo;
import com.objectvault.objectvault.services.Impl.UserServiceImpl;
import com.objectvault.objectvault.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    @GetMapping(
            path="/showuser/{id}",
            headers="accept="+ MediaType.APPLICATION_JSON_VALUE,
            produces= MediaType.APPLICATION_JSON_VALUE
    )
    public UserEntity getUsers(@PathVariable @NonNull String id){
        Optional<UserEntity> user = userService.getUser(id);
        if(user.isEmpty()){
            UserEntity userEntity = new UserEntity();
            return userEntity;
        }
        return user.get();
    }
}
