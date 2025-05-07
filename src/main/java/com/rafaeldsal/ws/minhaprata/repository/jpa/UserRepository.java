package com.rafaeldsal.ws.minhaprata.repository.jpa;

import com.rafaeldsal.ws.minhaprata.dto.user.UserBasicInfo;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  @Query("SELECT new com.rafaeldsal.ws.minhaprata.dto.user.UserBasicInfo(u.name, u.email) FROM User u WHERE u.email = :email")
  Optional<UserBasicInfo> findByEmail(String email);

  Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

  Boolean existsByEmail(String email);

  Boolean existsByCpf(String cpf);

  Boolean existsByPhoneNumber(String phoneNumber);

  Boolean existsByEmailAndIdNot(String email, String userId);

  Boolean existsByPhoneNumberAndIdNot(String phoneNumber, String userId);

}
