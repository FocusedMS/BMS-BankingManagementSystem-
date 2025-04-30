package com.bms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "full_name")
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @Column(name = "date_of_birth")
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Nationality is required")
    private String nationality;
    
    @Column(name = "phone_number")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    
    @Column(name = "permanent_address")
    @NotBlank(message = "Permanent address is required")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String permanentAddress;
    
    @Column(name = "government_issued_id")
    @NotBlank(message = "Government issued ID type is required")
    private String governmentIssuedID;
    
    @Column(name = "id_number")
    @NotBlank(message = "ID number is required")
    private String idNumber;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Account account;
} 