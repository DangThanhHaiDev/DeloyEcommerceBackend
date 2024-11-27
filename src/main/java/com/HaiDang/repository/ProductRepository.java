package com.HaiDang.repository;

import com.HaiDang.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p" +
            " where (p.category.name=:categoryName or :categoryName is null)" +
            " and ((:minPrice = 0 or :maxPrice = 0) or (p.discountedPrice between :minPrice and :maxPrice))" +
            " and (:minDiscount is null or p.discountPresent >= :minDiscount)" +
            " ORDER BY " +
            "CASE " +
            "WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
            "CASE " +
            "WHEN :sort = 'price_high' THEN p.discountedPrice END DESC")
    public List<Product> filterProducts(@Param("categoryName")String categoryName,
                                        @Param("minPrice") Double minPrice,
                                        @Param("maxPrice") Double maxPrice,
                                        @Param("minDiscount") Double minDiscount,
                                        @Param("sort") String sort);
    public Optional<Product> findById(Long productId);
    @Query("select p from Product p" +
            " where p.category.name=:categoryName")
    public List<Product> findByCategory(@Param("categoryName") String categoryName);
}
