package com.objectvault.objectvault.repositories;

import com.objectvault.objectvault.entity.UserEntity;

import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserid(String id);
}

