package com.fintrack.api.service;

import com.fintrack.api.dto.TransactionRequest;
import com.fintrack.api.dto.TransactionResponse;
import com.fintrack.api.entity.Category;
import com.fintrack.api.entity.Transaction;
import com.fintrack.api.entity.User;
import com.fintrack.api.repository.CategoryRepository;
import com.fintrack.api.repository.TransactionRepository;
import com.fintrack.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public TransactionResponse create(TransactionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = new Transaction();
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setUser(user);
        transaction.setCategory(category);

        Transaction saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    public List<TransactionResponse> getAllByUser(Long userId) {
        return transactionRepository.findByUserId(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TransactionResponse getById(Long id) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return toResponse(t);
    }

    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        t.setDescription(request.getDescription());
        t.setAmount(request.getAmount());
        t.setType(request.getType());
        t.setDate(request.getDate());
        t.setCategory(category);

        return toResponse(transactionRepository.save(t));
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getDescription(),
                t.getAmount(),
                t.getType(),
                t.getDate(),
                t.getCategory() != null ? t.getCategory().getName() : null,
                t.getUser().getId()
        );
    }
}