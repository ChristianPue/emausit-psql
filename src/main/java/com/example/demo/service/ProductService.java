package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Product;

public interface ProductService {
    public Product save(Product p);
    public Optional<Product> get(Integer id);
    public void update(Product p);
    public void delete(Integer id);
    public List<Product> findAll();
}
