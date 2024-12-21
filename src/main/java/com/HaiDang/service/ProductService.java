package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Product;
import com.HaiDang.model.User;
import com.HaiDang.request.CommentRequest;
import com.HaiDang.request.ProductExcelRequest;
import com.HaiDang.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {
    Product createProduct(ProductRequest productRequest) throws IOException;

    String deleteProduct(Long productId) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    Product updateProduct(Long productId, String title, String brand, String des, double price, double discountPercent,
                          double discountedPrice, int sizeS, int sizeM, int sizeL) throws ProductException;

    List<Product> findProductByCategory(String categoryName);

    PagedModel<Product> getAllProductsByFilter(String category, List<String> colors, List<String> sizes, Double minPrice, Double maxPrice, Double minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize, String title);
    List<Product> getAllProducts();
    Product addProductByExcel(ProductExcelRequest productRequest);
    PagedModel<Product> adminFilter(Long id, Integer pageNumber, Integer pageSize);
    Product addComment(Long productId, CommentRequest commentRequest, User user);
    List<Product> getSimilarProducts(Long productId);
}
