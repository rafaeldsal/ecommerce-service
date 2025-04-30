package com.rafaeldsal.ws.minhaprata.repository.jpa;

import com.rafaeldsal.ws.minhaprata.model.jpa.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
}
