package com.mastama.pasarbersama.repository;

import com.mastama.pasarbersama.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {

    Page<Categories> findByCategoryName(String categoryName, Pageable pageable);
}
