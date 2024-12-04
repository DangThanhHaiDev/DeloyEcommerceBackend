package com.HaiDang.service;

import com.HaiDang.model.Category;
import com.HaiDang.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public List<Category> getCategoryByLevel(int level) {
        List<Category> categories = categoryRepository.findByLevel(level);
        return categories;
    }

    @Override
    public List<Category> getCategoriesByParentCategoryId(Long id) {
        List<Category> categories = categoryRepository.findByParentCategoryId(id);
        return categories;
    }

}
