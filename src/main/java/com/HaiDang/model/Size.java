package com.HaiDang.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class Size {
    String name;
    String quantity;

    @Override
    public String toString() {
        return name+quantity;
    }
}
