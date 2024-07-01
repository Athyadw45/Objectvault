package com.objectvault.objectvault.services.Impl;

import com.objectvault.objectvault.dto.LoginUserDTO;
import com.objectvault.objectvault.dto.RegisterUserDTO;
import com.objectvault.objectvault.entity.UserEntity;
import com.objectvault.objectvault.repositories.UserRepo;
import com.objectvault.objectvault.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;



    @Override
    public Optional<UserEntity> getUser(String id) {
        return userRepo.findByUserid(id);
    }
    @Override
    public String register(RegisterUserDTO registerUserDTO){

        try{
            UserEntity user = new UserEntity();
            user.setFirstname(registerUserDTO.firstname());
            user.setLastname(registerUserDTO.lastname());
            user.setEmail(registerUserDTO.email());
            user.setPassword(passwordEncoder.encode(registerUserDTO.password()));
            UserEntity saveduser =  userRepo.save(user);
            return "User saved";
        }
        catch(Exception e){
            return e.toString();
        }

    }
    public Optional<UserDetails> loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username);
    }

    @Override
    public Optional<UserDetails> loginUser(LoginUserDTO loginUserDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserDTO.email(),
                            loginUserDTO.password()
                    )
            );

            return Optional.of(userRepo.findByEmail(loginUserDTO.email())
                    .orElseThrow());
        } catch (AuthenticationException e) {
            // Handle authentication failure
            return Optional.empty();
        } catch (Exception e) {
            // Handle any other exceptions that might occur
            return Optional.empty();
        }
    }
}
