package com.HaiDang.controller;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Category;
import com.HaiDang.model.Product;
import com.HaiDang.request.ProductExcelRequest;
import com.HaiDang.request.ProductRequest;
import com.HaiDang.response.ProductPageResponse;
import com.HaiDang.response.ProductResponse;
import com.HaiDang.response.SalesResponse;
import com.HaiDang.service.FileService;
import com.HaiDang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    @Autowired
    FileService fileService;
    @Autowired
    ProductService productService;
    @PostMapping("/")
    public ResponseEntity<Product> createProduct(
            @RequestBody ProductRequest productRequest) throws IOException {
        Product product = productService.createProduct(productRequest);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @PostMapping("/image")
    public  ResponseEntity<Product> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) throws IOException {
        Product product = fileService.upLoadImage(file, id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{productId}/delete")
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

    @PostMapping("/creates")
    public ResponseEntity<ProductResponse> createMultipleProducts(@RequestBody ProductExcelRequest productRequest[]) throws IOException {
        int row = 0;
        for(ProductExcelRequest product : productRequest){
            Product p = productService.addProductByExcel(product);
            if(p!= null){
                row++;
            }
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setSuccess(true);
        productResponse.setMessage("Products are created successfully");
        productResponse.setRow(row);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.CREATED);
    }
    @GetMapping("/filter")
    public ResponseEntity<ProductPageResponse> adminFilter(@RequestParam("category") Long category, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNumber") Integer pageNumber){
        PagedModel<Product> products = productService.adminFilter(category, pageNumber, pageSize);
        ProductPageResponse productPageResponse = new ProductPageResponse();
        productPageResponse.setTotalPages(products.getMetadata().getTotalPages());
        productPageResponse.setTotalElements(products.getMetadata().getTotalElements());
        List<Product> productList = StreamSupport.stream(products.spliterator(), false)
                .collect(Collectors.toList());
        productPageResponse.setProductList(productList);
        return new ResponseEntity<ProductPageResponse>(productPageResponse, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long id,@RequestParam("title") String title, @RequestParam("brand") String brand
    , @RequestParam("des") String des, @RequestParam("price") Double price, @RequestParam("discountPercent") Double discountPercent,
                                                 @RequestParam("discountedPrice") Double discountedPrice, @RequestParam("sizeS") Integer sizeS,
                                                 @RequestParam("sizeM") Integer sizeM, @RequestParam("sizeL") Integer sizeL) throws ProductException {
           Product product = productService.updateProduct(id, title, brand, des, price, discountPercent, discountedPrice, sizeS, sizeM, sizeL);
           return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
