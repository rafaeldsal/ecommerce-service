package com.rafaeldsal.ws.minhaprata.repository.jpa;

import com.rafaeldsal.ws.minhaprata.model.jpa.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserCredentials, Long> {

  Optional<UserCredentials> findByUsername(String username);
}
