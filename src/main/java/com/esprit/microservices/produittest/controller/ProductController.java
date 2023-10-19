package com.esprit.microservices.produittest.controller;

import com.esprit.microservices.produittest.model.Product;
import com.esprit.microservices.produittest.repository.ProductRepository;
import com.esprit.microservices.produittest.services.EmailService;
import com.esprit.microservices.produittest.services.ProductService;
import io.micrometer.core.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product addedProduct = productService.addProduct(product);
        // Envoyez une notification par e-mail
        String to = "emnaayachi2@gmail.com";
        String subject = "Nouveau produit ajouté";
        String message = "Un nouveau produit a été ajouté : " + product.getProductName() + "Reference:" + product.getReference() +"Description:"+product.getDescription();

        emailService.sendNotificationEmail(to, subject, message);
        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    //methode all avec pagination
    @GetMapping("/all")
    public List<Product> getAllProducts(@RequestParam int page, @RequestParam int pageSize) {
        List<Product> products = productService.getAllProducts(page, pageSize);
        return products;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        if (productService.updateProduct(id, updatedProduct)) {
            return ResponseEntity.ok("Product updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }
    //recherche avec 3 critéres et filtre avec prix min et max
    @GetMapping("/allSearchProduct")
    public List<Product> getAllSearchProduct(
            @RequestParam @Nullable String productName,
            @RequestParam @Nullable Integer reference,
            @RequestParam @Nullable String description,
            @RequestParam @Nullable Integer minPrice,
            @RequestParam @Nullable Integer maxPrice) {
        return productService.getAllSearchProduct(productName, reference, description, minPrice, maxPrice);
    }

    @GetMapping("/filter/price")
    public ResponseEntity<List<Product>> filterProductsByPrice(@RequestParam int maxPrice) {
        List<Product> filteredProducts = productService.filterProductsByPrice(maxPrice);
        return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
    }

    @GetMapping("/filter/quantity")
    public ResponseEntity<List<Product>> filterProductsByQuantity(@RequestParam int minQuantity) {
        List<Product> filteredProducts = productService.filterProductsByQuantity(minQuantity);
        return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
    }

}
