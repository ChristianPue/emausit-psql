package com.example.demo.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String show(Model model)
    {
        model.addAttribute("products", productService.findAll());
        return "products/show";
    }

    @GetMapping("/create")
    public String create()
    {
        return "products/create";
    }

    @PostMapping("/save")
    public String save(Product p)
    {
        LOGGER.info("Este es el objeto Product {}", p);
        
        User u = new User(1, "", "", "", "", "");
        p.setUser(u);
        productService.save(p);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model)
    {
        Optional<Product> optProduct = productService.get(id);
        Product p = optProduct.get();

        LOGGER.info("Producto bsucado: {}", p);
        model.addAttribute("product", p);

        return "products/edit";
    }

    @PostMapping("/update")
    public String update(Product p)
    {
        productService.update(p);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id)
    {
        productService.delete(id);
        return "redirect:/products";
    }
}
