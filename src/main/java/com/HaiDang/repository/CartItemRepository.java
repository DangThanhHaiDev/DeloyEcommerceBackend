package com.HaiDang.repository;

import com.HaiDang.model.Cart;
import com.HaiDang.model.CartItem;
import com.HaiDang.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("select c from CartItem c where c.cart=:cart and c.product=:product and c.size=:size and c.userId=:userId")
    public CartItem isCartItemExist(@Param("cart") Cart cart, @Param("product") Product product,
                                    @Param("size") String size, @Param("userId") Long userId);
    public Optional<CartItem> findById(Long id);
}
