package com.fintrack.api.dto;

import java.math.BigDecimal;
import java.util.Map;

public class DashboardResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private Map<String, BigDecimal> expenseByCategory;

    public DashboardResponse(BigDecimal totalIncome, BigDecimal totalExpense, Map<String, BigDecimal> expenseByCategory) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.expenseByCategory = expenseByCategory;
    }

    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    public BigDecimal getTotalExpense() { return totalExpense; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }
    public Map<String, BigDecimal> getExpenseByCategory() { return expenseByCategory; }
    public void setExpenseByCategory(Map<String, BigDecimal> expenseByCategory) { this.expenseByCategory = expenseByCategory; }
}
