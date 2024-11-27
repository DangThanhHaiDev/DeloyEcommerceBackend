package com.HaiDang.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String review;
    LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    Product product;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
