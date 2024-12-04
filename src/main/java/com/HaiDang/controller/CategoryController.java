package com.HaiDang.controller;

import com.HaiDang.model.Category;
import com.HaiDang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
