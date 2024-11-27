package com.HaiDang.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`Order`")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    LocalDateTime orderDate;
    LocalDateTime deliveryDate;
    double totalPrice;
    double discountedPrice;
    double discounted;
    String orderStatus;
    int totalItem;
    LocalDateTime createdAt;
    @OneToOne
    Address shppingAddress;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "user_id")
    User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> orderItems = new ArrayList<>();
    @Embedded
    PaymentDetails paymentDetails = new PaymentDetails();
}
