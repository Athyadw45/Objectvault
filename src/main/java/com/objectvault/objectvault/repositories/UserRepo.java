package com.objectvault.objectvault.repositories;

import com.objectvault.objectvault.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, String> {
}
