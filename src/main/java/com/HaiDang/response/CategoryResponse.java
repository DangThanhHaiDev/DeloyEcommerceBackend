package com.HaiDang.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    long id;
    String name;
    List<Section> sections;
    public CategoryResponse(){
        sections = new ArrayList<>();
    }
}
