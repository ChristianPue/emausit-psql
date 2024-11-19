package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("")
public class HomeController 
{
    private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductService productService;

    List<OrderDetail> orderdetails = new ArrayList<>();

    Order order = new Order();

    @GetMapping("")
    public String home(Model model)
    {
        model.addAttribute("products", productService.findAll());
        return "user/home";
    }

    @GetMapping("producthome/{id}")
    public String productHome(@PathVariable Integer id, Model model)
    {
        LOGGER.info("Id producto enviado como parámetro {}", id);

        Optional<Product> optProduct = productService.get(id);
        Product product = optProduct.get();

        model.addAttribute("producto", product);
        
        return "user/producthome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer quantity, Model model)
    {
        OrderDetail orderDetail = new OrderDetail();

        double total = 0;
        Optional<Product> optProduct = productService.get(id);
        LOGGER.info("Producto añadido: {}", optProduct);
        LOGGER.info("Cantidad: {}", quantity);

        Product product = optProduct.get();

        orderDetail.setName(product.getName());
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setTotalPrice(product.getPrice() * quantity);
        orderDetail.setProduct(product);

        total = orderdetails.stream().mapToDouble(dt -> dt.getTotalPrice()).sum();

        order.setTotalPrice(total);
        model.addAttribute("cart", orderdetails);
        model.addAttribute("orden", order);

        return "user/cart";
    }
}
