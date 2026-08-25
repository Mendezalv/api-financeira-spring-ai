package com.dio.budgetapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDateTime date;

    public Transaction() {}

    public Transaction(String description, BigDecimal amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDateTime getDate() { return date; }
}