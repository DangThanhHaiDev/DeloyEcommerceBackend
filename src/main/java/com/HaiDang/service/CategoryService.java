package com.HaiDang.service;

import com.HaiDang.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
     List<Category> getCategoryByLevel(int level);
     List<Category> getCategoriesByParentCategoryId(Long id);
}
