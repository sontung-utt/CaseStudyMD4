package com.codegym.casestudymd4.model;

import com.codegym.casestudymd4.model.validator.ValidPassword;
import com.codegym.casestudymd4.model.validator.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "staff_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @ValidUsername
    private String username;
    @NotBlank
    @ValidPassword
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created_at;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime modified_at;
    private String formattedCreatedAt;
    private String formattedModifiedAt;
    @PrePersist
    public void prePersist(){
        created_at = LocalDateTime.now();
        modified_at = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        modified_at = LocalDateTime.now();
    }
}
