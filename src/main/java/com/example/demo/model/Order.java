package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String number;
    private Date createdAt;
    private Date receivedAt;
    private double totalPrice;

    // Foreign Keys
    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "order")
    private OrderDetail details;

    // Constructor vacío
    public Order() {}

    // Constructor con parámetros
    public Order(Integer id, String number, Date createdAt, Date receivedAt, double totalPrice) {
        this.id = id;
        this.number = number;
        this.createdAt = createdAt;
        this.receivedAt = receivedAt;
        this.totalPrice = totalPrice;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderDetail getDetails() {
        return details;
    }

    public void setDetails(OrderDetail details) {
        this.details = details;
    }

    // Ver información
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order{");
        sb.append("id=").append(id);
        sb.append(", number=").append(number);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", receivedAt=").append(receivedAt);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", details=").append(details);
        sb.append('}');
        return sb.toString();
    }
}
