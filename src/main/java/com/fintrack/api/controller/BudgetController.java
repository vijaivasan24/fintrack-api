package com.fintrack.api.controller;

import com.fintrack.api.dto.BudgetRequest;
import com.fintrack.api.dto.BudgetResponse;
import com.fintrack.api.security.CustomUserDetails;
import com.fintrack.api.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> create(@RequestBody BudgetRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.create(request, userDetails.getUser().getId()));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(budgetService.getAllByUser(userDetails.getUser().getId()));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<BudgetResponse>> getByMonthAndYear(@RequestParam Integer month, @RequestParam Integer year, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(budgetService.getByMonthAndYear(userDetails.getUser().getId(), month, year));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(@PathVariable Long id, @RequestBody BudgetRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(budgetService.update(id, request, userDetails.getUser().getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        budgetService.delete(id, userDetails.getUser().getId());
        return ResponseEntity.noContent().build();
    }
}
