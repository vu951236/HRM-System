package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contract_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}
