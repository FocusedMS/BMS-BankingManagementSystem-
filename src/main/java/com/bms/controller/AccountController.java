package com.bms.controller;

import com.bms.dto.AccountCreationRequest;
import com.bms.dto.AccountCreationResponse;
import com.bms.dto.ErrorResponse;
import com.bms.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(
        summary = "Create a new bank account",
        description = "Creates a new bank account with the provided customer details"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Account created successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(hidden = true)
        )
    )
    @PostMapping("/create")
    public ResponseEntity<AccountCreationResponse> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        AccountCreationResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
} 