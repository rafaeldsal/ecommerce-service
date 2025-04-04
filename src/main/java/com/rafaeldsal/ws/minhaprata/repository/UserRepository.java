package com.rafaeldsal.ws.minhaprata.repository;

import com.rafaeldsal.ws.minhaprata.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Boolean existsByEmail(String email);

  Boolean existsByCpf(String cpf);

  Boolean existsByPhoneNumber(String phoneNumber);
}
