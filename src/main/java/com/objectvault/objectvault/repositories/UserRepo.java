/* (C) 2024 */
package com.objectvault.objectvault.repositories;

import com.objectvault.objectvault.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, String> {

  Optional<UserEntity> findByUserid(String id);

  Optional<UserDetails> findByEmail(String email);
}
