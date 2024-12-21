package com.HaiDang.request;

import com.HaiDang.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {
    Long id;
    String firstName;
    String lastName;
    String streetAddress;
    String city;
    String mobile;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    User user;
    boolean isExist;
}
