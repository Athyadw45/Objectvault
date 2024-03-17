package com.objectvault.objectvault.services.Impl;

import com.objectvault.objectvault.entity.UserEntity;
import com.objectvault.objectvault.repositories.UserRepo;
import com.objectvault.objectvault.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;



    @Override
    public Optional<UserEntity> getUser(String id) {
        return userRepo.findByUserid(id);
    }
    @Override
    public String register(UserEntity user){

        try{
            UserEntity saveduser =  userRepo.save(user);
            return "User saved";
        }
        catch(Exception e){
            return e.toString();
        }

    }
}
