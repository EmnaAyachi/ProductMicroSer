package com.esprit.microservices.produittest.repository;

import com.esprit.microservices.produittest.model.Product;
import io.micrometer.core.lang.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE (:productName IS NULL OR LOWER(p.productName) LIKE LOWER(:productName)) AND (:reference IS NULL OR p.reference = :reference) AND (:description IS NULL OR LOWER(p.description) LIKE LOWER(:description)) ORDER BY p.reference ASC")

    List<Product> getAllSearchProduct(@Param("productName") @Nullable String productName,
                                    @Param("reference") @Nullable Integer reference,
                                    @Param("description") @Nullable String description);

    List<Product> findByPriceLessThan(int maxPrice);
    List<Product> findByQuantityGreaterThan(int minQuantity);
}
