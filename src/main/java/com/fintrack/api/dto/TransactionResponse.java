package com.fintrack.api.dto;

import com.fintrack.api.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponse {
    private Long id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;
    private String categoryName;
    private Long userId;

    // Constructor
    public TransactionResponse(Long id, String description, BigDecimal amount,
                               TransactionType type, LocalDate date,
                               String categoryName, Long userId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.categoryName = categoryName;
        this.userId = userId;
    }

    // Getters
    public Long getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public LocalDate getDate() { return date; }
    public String getCategoryName() { return categoryName; }
    public Long getUserId() { return userId; }
}