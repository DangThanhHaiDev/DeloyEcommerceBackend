package com.HaiDang.repository;

import com.HaiDang.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o" +
            " where o.user.id = :userId" +
            " and o.orderStatus = 'PLACED' or o.orderStatus = 'CONFIRMED' or o.orderStatus = 'SHIPPED'")
    public List<Order> getUserOrders(@Param("userId") Long userId);
    Optional<Order> findById(Long orderId);
}
