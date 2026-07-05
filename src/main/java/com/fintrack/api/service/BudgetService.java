package com.fintrack.api.service;

import com.fintrack.api.dto.BudgetRequest;
import com.fintrack.api.dto.BudgetResponse;
import com.fintrack.api.entity.Budget;
import com.fintrack.api.entity.Category;
import com.fintrack.api.entity.User;
import com.fintrack.api.repository.BudgetRepository;
import com.fintrack.api.repository.CategoryRepository;
import com.fintrack.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public BudgetService(BudgetRepository budgetRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public BudgetResponse create(BudgetRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category);
        budget.setMonthlyLimit(request.getMonthlyLimit());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        return toResponse(budgetRepository.save(budget));
    }

    public List<BudgetResponse> getAllByUser(Long userId) {
        return budgetRepository.findByUserId(userId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }
    
    public List<BudgetResponse> getByMonthAndYear(Long userId, Integer month, Integer year) {
        return budgetRepository.findByUserIdAndMonthAndYear(userId, month, year).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public BudgetResponse update(Long id, BudgetRequest request, Long userId) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        
        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        budget.setCategory(category);
        budget.setMonthlyLimit(request.getMonthlyLimit());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        return toResponse(budgetRepository.save(budget));
    }

    public void delete(Long id, Long userId) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        
        if (!budget.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        
        budgetRepository.delete(budget);
    }

    private BudgetResponse toResponse(Budget b) {
        BudgetResponse r = new BudgetResponse();
        r.setId(b.getId());
        r.setCategoryId(b.getCategory().getId());
        r.setCategoryName(b.getCategory().getName());
        r.setMonthlyLimit(b.getMonthlyLimit());
        r.setMonth(b.getMonth());
        r.setYear(b.getYear());
        return r;
    }
}
