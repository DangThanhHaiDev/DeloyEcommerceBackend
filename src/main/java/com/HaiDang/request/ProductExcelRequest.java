package com.HaiDang.request;

import com.HaiDang.model.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor

public class ProductExcelRequest {
    String title;
    String description;
    double price;
    double discountPresent;
    int quantity;
    String brand;
    String color;
    Set<Size> sizes = new HashSet<>();
    String imageUrl;
    String topLevelCategory;
    String secondLevelCategory;
    String thirdLevelCategory;
}
