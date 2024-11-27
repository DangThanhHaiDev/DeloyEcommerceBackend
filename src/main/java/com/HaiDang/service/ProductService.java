package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Product;
import com.HaiDang.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product createProduct(ProductRequest productRequest);

    String deleteProduct(Long productId) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    Product updateProduct(Long productId, ProductRequest productRequest) throws ProductException;

    List<Product> findProductByCategory(String categoryName);

    PagedModel<Product> getAllProductsByFilter(String category, List<String> colors, List<String> sizes, Double minPrice, Double maxPrice, Double minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);
    List<Product> getAllProducts();
}
