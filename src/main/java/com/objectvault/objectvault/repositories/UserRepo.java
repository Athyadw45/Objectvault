package com.objectvault.objectvault.repositories;

import com.objectvault.objectvault.entity.UserEntity;

import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends CrudRepository<UserEntity, String> {

    Optional<UserEntity> findByUserid(String id);
    Optional<UserDetails> findByEmail(String email);
}

