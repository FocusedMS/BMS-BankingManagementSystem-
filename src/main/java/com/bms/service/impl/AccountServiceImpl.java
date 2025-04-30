package com.bms.service.impl;

import com.bms.dto.AccountCreationRequest;
import com.bms.dto.AccountCreationResponse;
import com.bms.entity.Account;
import com.bms.entity.Customer;
import com.bms.exception.ResourceNotFoundException;
import com.bms.repository.AccountRepository;
import com.bms.repository.CustomerRepository;
import com.bms.service.AccountService;
import com.bms.util.PasswordGenerator;
import com.bms.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordUtil passwordUtil;

    @Override
    @Transactional
    public AccountCreationResponse createAccount(AccountCreationRequest request) {
        // Check if ID number already exists
        if (customerRepository.existsByIdNumber(request.getIdNumber())) {
            throw new RuntimeException("ID number already registered");
        }

        LocalDateTime now = LocalDateTime.now();

        // Create customer
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setNationality(request.getNationality());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setPermanentAddress(request.getPermanentAddress());
        customer.setGovernmentIssuedID(request.getGovernmentIssuedID());
        customer.setIdNumber(request.getIdNumber());
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        // Generate password
        String password = generatePassword();
        String hashedPassword = passwordUtil.hashPassword(password);

        // Create account
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setPassword(hashedPassword);
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialDeposit());
        account.setAccountCreationDate(now);
        account.setCreatedAt(now);
        account.setUpdatedAt(now);
        account.setCustomer(customer);
        customer.setAccount(account);

        // Save customer and account
        customerRepository.save(customer);

        // Prepare response
        return new AccountCreationResponse(
                "Bank account created successfully.",
                password,
                new AccountCreationResponse.AccountDetails(
                        account.getAccountNumber(),
                        account.getCustomer().getFullName(),
                        account.getCustomer().getPhoneNumber(),
                        account.getCustomer().getPermanentAddress(),
                        account.getCustomer().getGovernmentIssuedID(),
                        account.getCustomer().getIdNumber(),
                        account.getAccountType().name(),
                        account.getBalance().doubleValue(),
                        account.getAccountCreationDate()
                )
        );
    }

    @Override
    public BigDecimal getAccountBalance(String accountNumber) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
        Account account = accountOptional.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return account.getBalance();
    }

    @Override
    public boolean accountExists(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).isPresent();
    }

    @Override
    @Transactional
    public void updateBalance(String accountNumber, BigDecimal amount) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
        Account account = accountOptional.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        
        BigDecimal newBalance = account.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "BANK" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private String generatePassword() {
        return PasswordGenerator.generatePassword();
    }
} 