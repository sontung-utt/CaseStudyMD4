package com.codegym.casestudymd4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Trường tên chức năng không được để trống!")
    private String name;
    @Column(name = "display_name")
    @NotBlank(message = "Trường tên hiển thị không được để trống!")
    private String displayName;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
