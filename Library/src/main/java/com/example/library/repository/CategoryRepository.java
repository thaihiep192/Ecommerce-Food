package com.example.library.repository;

import com.example.library.dto.CategoryDto;
import com.example.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "update  Category set name = ?1 where  id = ?2")
    Category update(String name, Long id);
    @Query(value = "select * from categories where  is_activated = true", nativeQuery = true)
    List<Category> findAllByaActivated();

    // Customer
    @Query("select new com.example.library.dto.CategoryDto(c.id,c.name, count(p.category.id))" +
            "from Category  c  left join Product p on c.id = p.category.id" +
            " where c.activated = true and c.deleted = false " +
            " group by c.id")
    List<CategoryDto> getCategoriesBySize();
}
