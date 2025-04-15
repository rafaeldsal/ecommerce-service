package com.rafaeldsal.ws.minhaprata.repository;

import com.rafaeldsal.ws.minhaprata.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserCredentials, Long> {

  Optional<UserCredentials> findByUsername(String username);
}
