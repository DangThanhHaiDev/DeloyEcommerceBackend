package com.HaiDang.service;

import com.HaiDang.model.Category;
import com.HaiDang.request.TwoLevelCategoryRequest;
import com.HaiDang.response.NavResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
     List<Category> getCategoryByLevel(int level);
     List<Category> getCategoriesByParentCategoryId(Long id);
     Category createCategory(String topCategory, List<TwoLevelCategoryRequest> twoLevelCategoryRequests);
     NavResponse getNav();
     List<Category> getAllCategory(int level);
}
