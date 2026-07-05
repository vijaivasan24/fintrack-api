package com.fintrack.api.repository;

import com.fintrack.api.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);
    List<Budget> findByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);
}
