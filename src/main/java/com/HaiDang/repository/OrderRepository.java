package com.HaiDang.repository;

import com.HaiDang.model.Order;
import com.HaiDang.response.SalesResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o" +
            " where o.user.id = :userId order by o.createdAt desc")
    public List<Order> getUserOrders(@Param("userId") Long userId);
    Optional<Order> findById(Long orderId);
    @Query("select o from Order o where o.createdAt>=:startDate and o.createdAt<=:endDate")
    List<Order> getOrderByFilter(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    @Query("select count(o.id) from Order o where o.user.id=:userId")
    int getNumberOrderByUser(@Param("userId") Long userId);
    @Query("select sum(o.discountedPrice) from Order o where o.user.id=:userId")
    Double getTotalMoneyByUser(@Param("userId")Long userId);
    @Query("select o from Order o where o.user.id=:userId order by o.createdAt desc")
    List<Order> getOrderByStatus(@Param("userId") Long userId);
    @Query("select new com.HaiDang.response.SalesResponse(MONTH(o.orderDate), SUM(o.discountedPrice))" +
            " from Order o where YEAR(o.orderDate)=:year and o.orderStatus='SHIPPED' " +
            "group by MONTH(o.orderDate) ")
    List<SalesResponse> findRevenueByYear(@Param("year")int year);

}
