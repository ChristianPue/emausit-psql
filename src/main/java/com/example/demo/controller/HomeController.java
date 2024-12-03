package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
@RequestMapping("/")
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

        model.addAttribute("product", product);
        
        return "user/producthome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer quantity, Model model)
    {
        OrderDetail orderDetail = new OrderDetail();

        Optional<Product> optProduct = productService.get(id);
        //LOGGER.info("Producto añadido: {}", optProduct.get());
        //LOGGER.info("Cantidad: {}", quantity);

        Product product = optProduct.get();

        orderDetail.setName(product.getName());
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setTotalPrice(product.getPrice() * quantity);
        
        orderDetail.setProduct(product);

        // Validación: Evitar repetir dos productos
        Integer idProduct = product.getId();
        boolean pass = orderdetails.stream().anyMatch(dt -> Objects.equals(dt.getProduct().getId(), idProduct));

        if (!pass)
        {
            orderdetails.add(orderDetail);
        }

        double total = orderdetails.stream().mapToDouble(dt -> dt.getTotalPrice()).sum();

        order.setTotalPrice(total);
        model.addAttribute("cart", orderdetails);
        model.addAttribute("dorder", order);

        return "user/cart";
    }

    @GetMapping("/delete/cart/{id}")
    public String deleteProductCart(@PathVariable Integer id, Model model)
    {
        List<OrderDetail> news = new ArrayList<>();

        for (OrderDetail orderDetail: orderdetails)
        {
            if (!Objects.equals(orderDetail.getProduct().getId(), id))
            {
                news.add(orderDetail);
            }
        }

        orderdetails = news;

        double total = orderdetails.stream().mapToDouble(dt -> dt.getTotalPrice()).sum();

        order.setTotalPrice(total);

        model.addAttribute("cart", orderdetails);
        model.addAttribute("dorder", order);

        return "user/cart";
    }

    @GetMapping("/getCart")
    public String getCart(Model model)
    {
        model.addAttribute("cart", orderdetails);
        model.addAttribute("dorder", order);

        return "user/cart";
    }
}
