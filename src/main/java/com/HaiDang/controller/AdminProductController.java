package com.HaiDang.controller;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Product;
import com.HaiDang.request.ProductRequest;
import com.HaiDang.response.ProductResponse;
import com.HaiDang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest){
        Product product = productService.createProduct(productRequest);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable("productId") Long productId) throws ProductException {
        String result = productService.deleteProduct(productId);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setMessage(result);
        productResponse.setSuccess(true);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }
    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductRequest productRequest) throws ProductException {
        Product newProduct = productService.updateProduct(productId, productRequest);
        return new ResponseEntity<Product>(newProduct, HttpStatus.OK);
    }
    @PostMapping("/creates")
    public ResponseEntity<ProductResponse> createMultipleProducts(@RequestBody ProductRequest productRequest[]){
        for(ProductRequest product : productRequest){
            productService.createProduct(product);
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setSuccess(true);
        productResponse.setMessage("Products are created successfully");
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.CREATED);
    }

}
