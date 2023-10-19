package com.esprit.microservices.produittest.feignclient;

import com.esprit.microservices.produittest.response.BlogResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "blog-service", url = "http://localhost:8020", path = "/esprit")
public interface BlogClient {
    @GetMapping("/blog/{idBlog}")
    public ResponseEntity<BlogResponse> getBlogByProductId(@PathVariable("idBlog") Long idBlog);
}
