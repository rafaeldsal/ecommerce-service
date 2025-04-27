package com.rafaeldsal.ws.minhaprata.repository.jpa;

import com.rafaeldsal.ws.minhaprata.model.enums.OrderStatus;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

  Page<Order> findAllByUserId(String userId, Pageable pageable);

  @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :orderId")
  Optional<Order> findByIdWithOrderItems(@Param("orderId") String orderId);

  @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = :status")
  Page<Order> findAllByUserIdAndStatus(@Param("userId") String userId, @Param("status") OrderStatus status, Pageable pageable);

  @Query("SELECT o FROM Order o WHERE o.status = :status")
  Page<Order> findAllByStatus(@Param("status") OrderStatus status, Pageable pageable);

}
