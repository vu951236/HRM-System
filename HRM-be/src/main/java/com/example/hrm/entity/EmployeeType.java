package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
}
