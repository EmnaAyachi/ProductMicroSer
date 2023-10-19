package com.esprit.microservices.produittest.services;

import com.esprit.microservices.produittest.feignclient.BlogClient;
import com.esprit.microservices.produittest.model.Product;
import com.esprit.microservices.produittest.repository.ProductRepository;
import com.esprit.microservices.produittest.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {


    @Autowired
    private ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BlogClient blogClient;



    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    //all avec pagination
    public List<Product> getAllProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("reference"));
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.getContent();
    }

    public boolean updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product productToUpdate = existingProduct.get();
            productToUpdate.setProductName(updatedProduct.getProductName());
            productToUpdate.setDescription(updatedProduct.getDescription());
            productToUpdate.setPrice(updatedProduct.getPrice());
            productToUpdate.setReference(updatedProduct.getReference());
            productToUpdate.setQuantity(updatedProduct.getQuantity());

            productRepository.save(productToUpdate);
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteProduct(Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
//recherche par 3 critéres
 public List<Product> getAllSearchProduct(
        String productName,
        Integer reference,
        String description,
        Integer minPrice,
        Integer maxPrice) {
    String jpql = "SELECT p FROM Product p " +
            "WHERE (:productName IS NULL OR LOWER(p.productName) LIKE LOWER(:productName)) " +
            "AND (:reference IS NULL OR p.reference = :reference) " +
            "AND (:description IS NULL OR LOWER(p.description) LIKE LOWER(:description)) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "ORDER BY p.reference ASC";

    TypedQuery<Product> typedQuery = entityManager.createQuery(jpql, Product.class);

    typedQuery.setParameter("productName", productName);
    typedQuery.setParameter("reference", reference);
    typedQuery.setParameter("description", description);
    typedQuery.setParameter("minPrice", minPrice);
    typedQuery.setParameter("maxPrice", maxPrice);

    return typedQuery.getResultList();
}

    public List<Product> filterProductsByPrice(int maxPrice) {
        return productRepository.findByPriceLessThan(maxPrice);
    }

    public List<Product> filterProductsByQuantity(int minQuantity) {
        return productRepository.findByQuantityGreaterThan(minQuantity);
    }



  //**  public ProductResponse getProductById(Long id) {

      //  Optional<Product> product = productRepository.findById(id);
       // ProductResponse productResponse = mapper.map(employee, EmployeeResponse.class);

        // Using FeignClient
      //  ResponseEntity<AddressResponse> addressResponse = addressClient.getAddressByEmployeeId(id);
     //   employeeResponse.setAddressResponse(addressResponse.getBody());

      //  return employeeResponse;
   // }

//}
}
