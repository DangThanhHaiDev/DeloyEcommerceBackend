package com.HaiDang.controller;

import com.HaiDang.model.Category;
import com.HaiDang.request.CategoryRequest;
import com.HaiDang.response.NavResponse;
import com.HaiDang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/{level}")
    public ResponseEntity<List<Category>> getCategoriesByLevel(@PathVariable("level") int level){
        List<Category> categories = categoryService.getCategoryByLevel(level);
        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }
    @GetMapping("/parent/{parentCategoryId}")
    public ResponseEntity<List<Category>> getCategoriesByParentId(@PathVariable("parentCategoryId") Long id){
        List<Category> categories = categoryService.getCategoriesByParentCategoryId(id);
        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest){
        Category category = categoryService.createCategory(categoryRequest.getTopCategory(), categoryRequest.getTwoLevelCategoryRequests());
        if(category!=null){
            return new ResponseEntity<>(category, HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getCategory(){
        List<Category> categories = categoryService.getAllCategory(3);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
