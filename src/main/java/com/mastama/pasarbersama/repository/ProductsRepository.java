package com.mastama.pasarbersama.repository;

import com.mastama.pasarbersama.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    // naming method
//    Page<Products> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    // query method
    @Query("""
    SELECT product 
    FROM Products product 
    WHERE LOWER(product.productName) LIKE LOWER(CONCAT('%', :query, '%'))
    AND product.categoryId.id = :categoryId
""")
    Page<Products> filterByNameAndCategory(String query, Integer categoryId, Pageable pageable);

    @Query("""
    SELECT product 
    FROM Products product 
    WHERE (LOWER(product.productName) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(product.description) LIKE LOWER(CONCAT('%', :query, '%')))
""")
    Page<Products> filterByNameOrDescription(String query, Pageable pageable);

    @Query("""
    SELECT product 
    FROM Products product 
    WHERE (LOWER(product.productName) LIKE LOWER(CONCAT('%', :query, '%'))
    OR LOWER(product.description) LIKE LOWER(CONCAT('%', :query, '%')))
    AND product.categoryId.id = :categoryId
""")
    Page<Products> filterByNameOrDescriptionAndCategory(String query, Integer categoryId, Pageable pageable);

    @Query("""
    SELECT product 
    FROM Products product 
    WHERE product.categoryId.id = :categoryId
""")
    Page<Products> filterByCategory(Integer categoryId, Pageable pageable);

}
