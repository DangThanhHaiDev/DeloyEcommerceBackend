package com.HaiDang.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    double rating;
    LocalDateTime createdAt;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    Product product;
}
