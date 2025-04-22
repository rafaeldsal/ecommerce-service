package com.rafaeldsal.ws.minhaprata.repository.redis;

import com.rafaeldsal.ws.minhaprata.model.redis.UserRecoveryCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRecoveryCodeRepository extends CrudRepository<UserRecoveryCode, String> {

  Optional<UserRecoveryCode> findByEmail(String email);
}
