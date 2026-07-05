package com.fintrack.api;

import com.fintrack.api.controller.AuthController;
import com.fintrack.api.controller.BudgetController;
import com.fintrack.api.controller.CategoryController;
import com.fintrack.api.controller.DashboardController;
import com.fintrack.api.controller.TransactionController;
import com.fintrack.api.dto.*;
import com.fintrack.api.entity.TransactionType;
import com.fintrack.api.entity.User;
import com.fintrack.api.repository.UserRepository;
import com.fintrack.api.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ApiIntegrationTest {

    @Autowired
    private AuthController authController;
    @Autowired
    private CategoryController categoryController;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private BudgetController budgetController;
    @Autowired
    private DashboardController dashboardController;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFullFlow() {
        // 1. Register
        RegisterRequest register = new RegisterRequest();
        register.setUsername("flowuser");
        register.setEmail("flowuser@example.com");
        register.setPassword("password");
        authController.register(register);

        // Retrieve user to mock CustomUserDetails
        User user = userRepository.findByEmail("flowuser@example.com").orElseThrow();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 3. Create Category
        CategoryRequest catReq = new CategoryRequest();
        catReq.setName("Food");
        catReq.setDescription("Groceries and Dining");
        ResponseEntity<CategoryResponse> catResult = categoryController.create(catReq, userDetails);
        assertEquals(HttpStatus.CREATED, catResult.getStatusCode());

        // 4. Create Transaction
        TransactionRequest transReq = new TransactionRequest();
        transReq.setAmount(new BigDecimal("50.00"));
        transReq.setCategoryId(catResult.getBody().getId());
        transReq.setDate(LocalDate.now());
        transReq.setDescription("Lunch");
        transReq.setType(TransactionType.EXPENSE);
        ResponseEntity<TransactionResponse> transResult = transactionController.create(transReq, userDetails);
        assertEquals(HttpStatus.CREATED, transResult.getStatusCode());

        // 5. Create Budget
        BudgetRequest budgetReq = new BudgetRequest();
        budgetReq.setCategoryId(catResult.getBody().getId());
        budgetReq.setMonthlyLimit(new BigDecimal("500.00"));
        budgetReq.setMonth(LocalDate.now().getMonthValue());
        budgetReq.setYear(LocalDate.now().getYear());
        ResponseEntity<BudgetResponse> budgetResult = budgetController.create(budgetReq, userDetails);
        assertEquals(HttpStatus.CREATED, budgetResult.getStatusCode());

        // 6. Get Dashboard Summary
        ResponseEntity<DashboardResponse> summaryResult = dashboardController.getSummary(
                LocalDate.now().getMonthValue(), LocalDate.now().getYear(), userDetails);
        assertEquals(HttpStatus.OK, summaryResult.getStatusCode());
    }
}
