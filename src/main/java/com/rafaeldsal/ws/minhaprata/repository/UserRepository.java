package com.rafaeldsal.ws.minhaprata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
