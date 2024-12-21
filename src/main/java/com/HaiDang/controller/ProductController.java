package com.HaiDang.controller;

import com.HaiDang.exception.ProductException;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.Product;
import com.HaiDang.model.Size;
import com.HaiDang.model.User;
import com.HaiDang.request.CommentRequest;
import com.HaiDang.request.ProductRequest;
import com.HaiDang.response.ProductPageResponse;
import com.HaiDang.response.ProductResponse;
import com.HaiDang.service.ProductService;
import com.HaiDang.service.ProductServiceImpl;
import com.HaiDang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class ProductController{
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @GetMapping("/products")
    public ResponseEntity<ProductPageResponse> getProductByFilter(@RequestParam(required = false) String category, @RequestParam(required = false) List<String> color, @RequestParam(required = false) List<String> size,
                                                            @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) Double minDiscount,
                                                            @RequestParam(required = false) String sort, @RequestParam(required = false) String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize, @RequestParam String title){
        PagedModel<Product> products = productService.getAllProductsByFilter(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize, title);
        ProductPageResponse productPageResponse = new ProductPageResponse();
        productPageResponse.setTotalPages(products.getMetadata().getTotalPages());
        productPageResponse.setTotalElements(products.getMetadata().getTotalElements());
        List<Product> productList = StreamSupport.stream(products.spliterator(), false)
                .collect(Collectors.toList());
        productPageResponse.setProductList(productList);
        return new ResponseEntity<ProductPageResponse>(productPageResponse, HttpStatus.OK);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }
    @PostMapping("/product/rating/{productId}")
    public ResponseEntity<Product> ratingProduct(@PathVariable("productId") Long productId, @RequestBody CommentRequest comment, @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Product product = productService.addComment(productId, comment, user);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products/similar/{productId}")
    public ResponseEntity<List<Product>> getSimilarProducts(@PathVariable("productId") Long productId){
        List<Product> products = productService.getSimilarProducts(productId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
