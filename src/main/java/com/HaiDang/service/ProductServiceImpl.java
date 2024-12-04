package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Category;
import com.HaiDang.model.Product;
import com.HaiDang.repository.CategoryRepository;
import com.HaiDang.repository.ProductRepository;
import com.HaiDang.request.ProductRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public Product createProduct(ProductRequest productRequest) {
        Optional<Category> optionalTopCategory = categoryRepository.findById(productRequest.getTopLevelCategory());
        Optional<Category> optionalTwoCategory = categoryRepository.findById(productRequest.getSecondLevelCategory());
        Optional<Category> optionalThreeCategory = categoryRepository.findById(productRequest.getThirdLevelCategory());
        if(!optionalTopCategory.isPresent() || !optionalTwoCategory.isPresent() || !optionalThreeCategory.isPresent()){
            return null;
        }
        Category levelParent = optionalThreeCategory.get();

        Product newProduct = new Product();
                newProduct.setQuantity(productRequest.getQuantity());
                newProduct.setImageUrl(productRequest.getImageUrl());
                newProduct.setSize(productRequest.getSizes());
                newProduct.setPrice(productRequest.getPrice());
                newProduct.setCreatedAt(LocalDateTime.now());
                newProduct.setBrand(productRequest.getBrand());
                newProduct.setColor(productRequest.getColor());
                newProduct.setTitle(productRequest.getTitle());
                newProduct.setDiscountPresent(productRequest.getDiscountPresent());
                newProduct.setDiscountedPrice(productRequest.getPrice() - productRequest.getDiscountPresent());
                newProduct.setDescription(productRequest.getDescription());
                newProduct.setCategory(levelParent);
                newProduct.setDelete(false);

        return productRepository.save(newProduct);
    }

    @Override
    @Transactional
    public String deleteProduct(Long productId) throws ProductException{
        try {
            Product product = findProductById(productId);
            product.getSize().clear();
            productRepository.deleteSizesByProductId(productId);
            productRepository.delete(product);
        }catch (Exception e){
            productRepository.deleteProduct(productId);
        }

        return productId+"";
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
       Optional<Product> optionalProduct =  productRepository.findById(productId);
       if(optionalProduct.isPresent()){
           return optionalProduct.get();
       }
       throw new ProductException("Product not found with Id: "+productId);
    }

    @Override
    public Product updateProduct(Long productId, ProductRequest productRequest) throws ProductException {
        Product product = findProductById(productId);
        if(productRequest.getQuantity() != 0){
            product.setQuantity(productRequest.getQuantity());
        }
        return productRepository.save(product);
    }

    @Override
    public List<Product> findProductByCategory(String categoryName) {
        List<Product> products = productRepository.findByCategory(categoryName);
        return products;
    }

    @Override
    public PagedModel<Product> getAllProductsByFilter(String category, List<String> colors, List<String> sizes, Double minPrice, Double maxPrice, Double minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
            if(!colors.get(0).equals("null")){
                products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                        .collect(Collectors.toList());
            }




//        if(!stock.equals("")){
//            if(stock.equals("in_stock")){
//                products = products.stream().filter(p -> p.getQuantity() > 0)
//                        .collect(Collectors.toList());
//            }
//            else if(stock.equals("out_of_stock")){
//                products = products.stream().filter(p -> p.getQuantity() < 1)
//                        .collect(Collectors.toList());
//            }
//        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
        List<Product> productFiltered = products.subList(startIndex ,endIndex);
        Page<Product> productPage = new PageImpl<>(productFiltered, pageable, products.size());
        PagedModel<Product> pagedModel = PagedModel.of(productPage.getContent(), new PagedModel.PageMetadata(productPage.getSize(), productPage.getNumber(), productPage.getTotalElements(), productPage.getTotalPages()));
        return pagedModel;
    }

    public List<Product> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products;
    }
}
