package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.*;
import com.HaiDang.repository.CategoryRepository;
import com.HaiDang.repository.ProductRepository;
import com.HaiDang.repository.RatingRepository;
import com.HaiDang.repository.ReviewRepository;
import com.HaiDang.request.CommentRequest;
import com.HaiDang.request.ProductExcelRequest;
import com.HaiDang.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Override
    public Product createProduct(ProductRequest productRequest) throws IOException {

        Optional<Category> optionalTopCategory = categoryRepository.findById(productRequest.getTopLevelCategory());
        Optional<Category> optionalTwoCategory = categoryRepository.findById(productRequest.getSecondLevelCategory());
        Optional<Category> optionalThreeCategory = categoryRepository.findById(productRequest.getThirdLevelCategory());
        if(!optionalTopCategory.isPresent() || !optionalTwoCategory.isPresent() || !optionalThreeCategory.isPresent()){
            return null;
        }
        Category levelParent = optionalThreeCategory.get();

        Product newProduct = new Product();
                newProduct.setQuantity(productRequest.getQuantity());
//                newProduct.setImageUrl(productRequest.getImageUrl());
                newProduct.setSize(productRequest.getSizes());
                newProduct.setPrice(productRequest.getPrice());
                newProduct.setCreatedAt(LocalDateTime.now());
                newProduct.setBrand(productRequest.getBrand());
                newProduct.setColor(productRequest.getColor());
                newProduct.setTitle(productRequest.getTitle());
                newProduct.setDiscountPresent(productRequest.getDiscountPresent());
        newProduct.setDiscountedPrice(productRequest.getPrice() * (((100- productRequest.getDiscountPresent())/100.0)));
                newProduct.setDescription(productRequest.getDescription());
                newProduct.setCategory(levelParent);


        newProduct.setDelete(false);

        return productRepository.save(newProduct);
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException{
        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()){
            return "Hai";
        }
        Product p = product.get();
        p.setDelete(true);
        productRepository.save(p);
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
    public Product updateProduct(Long productId, String title, String brand, String des, double price, double discountPercent, double discountedPrice, int sizeS, int sizeM, int sizeL) throws ProductException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(!productOptional.isPresent()){
            return null;
        }
        Product product = productOptional.get();
        product.setTitle(title);
        product.setBrand(brand);
        product.setDescription(des);
        product.setPrice(price);
        product.setDiscountPresent(discountPercent);
        product.setDiscountedPrice(discountedPrice);
        Size sizeSmall = new Size();
        sizeSmall.setName("S");
        sizeSmall.setQuantity(sizeS);
        Size sizeMedium = new Size();
        sizeMedium.setName("M");
        sizeMedium.setQuantity(sizeM);
        Size sizeLarge = new Size();
        sizeLarge.setName("L");
        sizeLarge.setQuantity(sizeL);
        product.getSize().clear();
        product.getSize().add(sizeSmall);
        product.getSize().add(sizeMedium);
        product.getSize().add(sizeLarge);
        product.setQuantity(sizeS + sizeM + sizeL);
        return productRepository.save(product);
    }


    @Override
    public List<Product> findProductByCategory(String categoryName) {
        List<Product> products = productRepository.findByCategory(categoryName);
        return products;
    }

    @Override
    public PagedModel<Product> getAllProductsByFilter(String category, List<String> colors, List<String> sizes, Double minPrice, Double maxPrice, Double minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize, String title) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort, title);
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

    @Override
    public Product addProductByExcel(ProductExcelRequest productRequest) {
        Category top = categoryRepository.findByNameAndLevel(productRequest.getTopLevelCategory(), 1);
        Category two = categoryRepository.findByNameAndParent(productRequest.getSecondLevelCategory(), productRequest.getTopLevelCategory());
        Category three = categoryRepository.findByNameAndParent(productRequest.getThirdLevelCategory(), productRequest.getSecondLevelCategory());
        System.out.println(three);
        System.out.println(two);

        if(top == null || two == null || three == null){
            return null;
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
        newProduct.setDiscountedPrice(productRequest.getPrice() * (((100- productRequest.getDiscountPresent())/100.0)));
        newProduct.setDescription(productRequest.getDescription());
        newProduct.setCategory(three);
        return productRepository.save(newProduct);
    }

    @Override
    public PagedModel<Product> adminFilter(Long id, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.adminFilter(id);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
        List<Product> productFiltered = products.subList(startIndex ,endIndex);
        Page<Product> productPage = new PageImpl<>(productFiltered, pageable, products.size());
        PagedModel<Product> pagedModel = PagedModel.of(productPage.getContent(), new PagedModel.PageMetadata(productPage.getSize(), productPage.getNumber(), productPage.getTotalElements(), productPage.getTotalPages()));
        return pagedModel;
    }

    @Override
    public Product addComment(Long productId, CommentRequest comment, User user) {
        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()){
            return null;
        }
        Product newProduct = product.get();

        Rating rating = new Rating();
        rating.setProduct(newProduct);
        rating.setUser(user);
        rating.setRating(comment.getQuality());
        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);

        Review review = new Review();
        review.setProduct(newProduct);
        review.setUser(user);
        review.setReview(comment.getComment());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        newProduct.getRatings().add(rating);
        newProduct.getReviews().add(review);
        return newProduct;
    }

    @Override
    public List<Product> getSimilarProducts(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return productRepository.findSimilarProducts(product.get().getCategory().getId());
    }
}
