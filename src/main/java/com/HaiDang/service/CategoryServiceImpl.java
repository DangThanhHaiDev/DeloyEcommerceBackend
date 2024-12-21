package com.HaiDang.service;

import com.HaiDang.model.Category;
import com.HaiDang.repository.CategoryRepository;
import com.HaiDang.request.TwoLevelCategoryRequest;
import com.HaiDang.response.CategoryResponse;
import com.HaiDang.response.NavResponse;
import com.HaiDang.response.Section;
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

    @Override
    public Category createCategory(String topCategory, List<TwoLevelCategoryRequest> twoLevelCategoryRequests) {
        Category top = categoryRepository.findByName(topCategory);
        if(top!=null && top.getLevel()==1){
            return null;
        }
        Category category = new Category();
        category.setParentCategory(null);
        category.setLevel(1);
        category.setName(topCategory);
        categoryRepository.save(category);


        for (TwoLevelCategoryRequest c2: twoLevelCategoryRequests) {
            Category category1 = new Category();
            category1.setParentCategory(category);
            category1.setLevel(2);
            category1.setName(c2.getName());
            categoryRepository.save(category1);

            for (String c3: c2.getThirdLevel()) {
                Category category2 = new Category();
                category2.setParentCategory(category1);
                category2.setName(c3);
                category2.setLevel(3);
                categoryRepository.save(category2);
            }
        }
        return category;
    }

    @Override
    public NavResponse getNav() {
        NavResponse nav = new NavResponse();

        List<Category> top = categoryRepository.findByLevel(1);
        for(Category c: top){
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(c.getId());
            categoryResponse.setName(c.getName());

            List<Category> two = categoryRepository.findByParentAndLevel(c.getName(), 2);
            for (Category c2 : two) {
                Section section = new Section();
                section.setId(c2.getId());
                section.setName(c2.getName());
                List<String> three = categoryRepository.findNameByParent(c.getName(), c2.getName());
                section.setItems(three);
                categoryResponse.getSections().add(section);
            }
            nav.getNavList().add(categoryResponse);
        }
    return nav;
    }

    @Override
    public List<Category> getAllCategory(int level) {
        return categoryRepository.findByLevel(level);
    }

}
