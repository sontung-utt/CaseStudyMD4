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
@Table(name = "customer_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Trường tên tài khoản không được để trống!")
    @ValidUsername
    private String username;
    @NotBlank(message = "Trường mật khẩu không được để trống!")
    @ValidPassword
    private String password;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created_at;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime modified_at;
    private String formattedCreatedAt;
    private String formattedModifiedAt;

    public CustomerAccount(Long id, String username, String password, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    }

    public CustomerAccount(String username, String password) {
    }

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
