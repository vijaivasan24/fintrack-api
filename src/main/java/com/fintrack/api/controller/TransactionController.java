package com.fintrack.api.controller;

import com.fintrack.api.dto.TransactionRequest;
import com.fintrack.api.dto.TransactionResponse;
import com.fintrack.api.service.TransactionService;
import com.fintrack.api.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        request.setUserId(userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(transactionService.getAllByUser(userDetails.getUser().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // ideally service should verify this transaction belongs to the user
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
                                                      @RequestBody TransactionRequest request,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        request.setUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // ideally service should verify this transaction belongs to the user before deleting
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}