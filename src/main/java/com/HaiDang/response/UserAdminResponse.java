package com.HaiDang.response;

import com.HaiDang.model.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAdminResponse {
    long totalElements;
    List<User> users;
    long totalPages;
}
