package com.fintrack.api.controller;

import com.fintrack.api.dto.DashboardResponse;
import com.fintrack.api.entity.Transaction;
import com.fintrack.api.entity.TransactionType;
import com.fintrack.api.repository.TransactionRepository;
import com.fintrack.api.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final TransactionRepository transactionRepository;

    public DashboardController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardResponse> getSummary(@RequestParam Integer month, 
                                                        @RequestParam Integer year,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(
                userDetails.getUser().getId(), startDate, endDate);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> expenseByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE && t.getCategory() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        return ResponseEntity.ok(new DashboardResponse(totalIncome, totalExpense, expenseByCategory));
    }
}
