package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Category;
import com.HaiDang.model.Product;
import com.HaiDang.repository.CategoryRepository;
import com.HaiDang.repository.ProductRepository;
import com.HaiDang.request.ProductRequest;
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
        Category topLevel = categoryRepository.findByName(productRequest.getTopLevelCategory());
        if(topLevel == null){
            topLevel = new Category();
                    topLevel.setLevel(1);
                    topLevel.setName(productRequest.getTopLevelCategory());
            categoryRepository.save(topLevel);
        }
        Category secondCategory = categoryRepository.findByNameAndParent(productRequest.getSecondLevelCategory(), topLevel.getName());
        if(secondCategory == null){
            secondCategory = new Category();
                   secondCategory.setLevel(2);
                   secondCategory.setName(productRequest.getSecondLevelCategory());
                   secondCategory.setParentCategory(topLevel);
            categoryRepository.save(secondCategory);
        }
        Category thirdCategory = categoryRepository.findByNameAndParent(productRequest.getThirdLevelCategory(), secondCategory.getName());
        if(thirdCategory == null){
            thirdCategory = new Category();
                    thirdCategory.setLevel(3);
                    thirdCategory.setName(productRequest.getThirdLevelCategory());
                    thirdCategory.setParentCategory(secondCategory);
            categoryRepository.save(thirdCategory);
        }
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
                newProduct.setCategory(thirdCategory);

        return productRepository.save(newProduct);
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException{
        Product product = findProductById(productId);
        product.getSize().clear();
        productRepository.delete(product);
        return "Product Deleted Successfully!";
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
