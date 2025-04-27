package com.rafaeldsal.ws.minhaprata.repository.jpa;

import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

  Boolean existsByEmail(String email);

  Boolean existsByCpf(String cpf);

  Boolean existsByPhoneNumber(String phoneNumber);

  Boolean existsByEmailAndIdNot(String email, String userId);

  Boolean existsByPhoneNumberAndIdNot(String phoneNumber, String userId);

}
