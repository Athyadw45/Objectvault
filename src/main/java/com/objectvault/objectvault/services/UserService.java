package com.objectvault.objectvault.services;

import com.objectvault.objectvault.entity.UserEntity;

import java.util.Optional;

public interface UserService {

    public Optional<UserEntity> getUser(String id);

}
