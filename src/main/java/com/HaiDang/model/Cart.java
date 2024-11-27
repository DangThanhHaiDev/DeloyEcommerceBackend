package com.HaiDang.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    double totalPrice;
    int totalItem;
    double totalDiscountedPrice;
    double discounted;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CartItem> cartItems = new HashSet<>();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart cart = (Cart) o;
        return Double.compare(cart.totalPrice, totalPrice) == 0 &&
                totalItem == cart.totalItem &&
                Double.compare(cart.totalDiscountedPrice, totalDiscountedPrice) == 0 &&
                Double.compare(cart.discounted, discounted) == 0 &&
                id != null && id.equals(cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalPrice, totalItem, totalDiscountedPrice, discounted);
    }

}
