package com.HaiDang.controller;

import com.HaiDang.response.NavResponse;
import com.HaiDang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NavController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/nav")
    public ResponseEntity<NavResponse> getNav(){
        NavResponse navResponse = categoryService.getNav();
        return new ResponseEntity<>(navResponse, HttpStatus.OK);
    }
}
