package com.HaiDang.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Section {
    long id;
    String name;
    List<String> items;
    public Section(){
        items = new ArrayList<>();
    }
}
