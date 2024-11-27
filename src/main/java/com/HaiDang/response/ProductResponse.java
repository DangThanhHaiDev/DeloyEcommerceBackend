package com.HaiDang.response;

import com.HaiDang.model.Product;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    boolean isSuccess;
    String message;
    Product product;
}
