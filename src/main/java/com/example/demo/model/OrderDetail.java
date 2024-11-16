package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetail {
    // Parámetros
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private double quantity;
    private double price;
    private double totalPrice;

    // Foreign Keys
    @OneToOne
    private Order order;
    @OneToOne
    private Product product;

    // Constructor vacío
    public OrderDetail() {}

    // Constructor con parámetros
    public OrderDetail(Integer id, String name, Order order, double price, Product product, double quantity, double totalPrice) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.price = price;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters y Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Ver información
    @Override
    public String toString() {
        return "OrderDetail [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price
                + ", totalPrice=" + totalPrice + ", order=" + order + ", product=" + product + "]";
    }
}
