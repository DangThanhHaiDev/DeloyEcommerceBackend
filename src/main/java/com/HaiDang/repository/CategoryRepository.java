package com.HaiDang.repository;

import com.HaiDang.model.Category;
import com.HaiDang.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    @Query("select c from Category c where c.name=:name and c.level=:level")
    Category findByNameAndLevel(@Param("name")String name, @Param("level") int level);
    @Query("select c from Category c" +
            " where c.name=:name and c.parentCategory.name=:parent")
    Category findByNameAndParent(@Param("name") String name, @Param("parent") String parent);
    List<Category> findByLevel(int level);
    @Query("select c from Category c where c.parentCategory.id=:parentCategoryId")
    List<Category> findByParentCategoryId(@Param("parentCategoryId") Long parentCategoryId);
    @Query("select c from Category c where c.parentCategory.name=:parent and c.level=:level")
    List<Category> findByParentAndLevel(@Param("parent") String parent, @Param("level") int level);
    @Query("select c.name from Category c where c.parentCategory.parentCategory.name=:parent1 and c.parentCategory.name=:parent2")
    List<String> findNameByParent(@Param("parent1") String parent1, @Param("parent2") String parent2);

}
