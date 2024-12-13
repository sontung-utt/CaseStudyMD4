package com.codegym.casestudymd4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Trường tên nhân sự không được để trống!")
    private String name;
    @NotBlank(message = "Trường giới tính không được để trống!")
    private String gender;
    @NotNull(message = "Trường ngày sinh không được để trống!")
    private LocalDate birth;
    private String image;
    @NotBlank(message = "Trường địa chỉ không được để trống!")
    private String address;
    @NotBlank(message = "Trường email không được để trống!")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Email nhập sai định dạng! Yêu cầu nhập theo định dạng test@gmail.com")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Trường số điện thoại không được để trống!")
    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Số điện thoại nhập sai định dạng!")
    @Column(unique=true)
    private String phone;
    @NotNull(message = "Trường tiền lương không được để trống!")
    private BigDecimal salary;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private StaffAccount staffAccount;
}
