package com.fintrack.api.dto;

import com.fintrack.api.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRequest {
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;
    private Long categoryId;
    private Long userId;

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}