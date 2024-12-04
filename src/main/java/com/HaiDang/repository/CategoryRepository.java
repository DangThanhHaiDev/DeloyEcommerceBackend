package com.HaiDang.repository;

import com.HaiDang.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    @Query("select c from Category c" +
            " where c.name=:name and c.parentCategory.name=:parent")
    Category findByNameAndParent(@Param("name") String name, @Param("parent") String parent);
    List<Category> findByLevel(int level);
    @Query("select c from Category c where c.parentCategory.id=:parentCategoryId")
    List<Category> findByParentCategoryId(@Param("parentCategoryId") Long parentCategoryId);
}
