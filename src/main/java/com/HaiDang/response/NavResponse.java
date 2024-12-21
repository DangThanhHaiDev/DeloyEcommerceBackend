package com.HaiDang.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NavResponse {
    List<CategoryResponse> navList;
    public NavResponse(){
        navList = new ArrayList<>();
    }
}
