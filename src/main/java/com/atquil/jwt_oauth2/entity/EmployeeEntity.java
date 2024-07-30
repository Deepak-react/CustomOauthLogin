package com.atquil.jwt_oauth2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

@Table(name="EMP_INFO")
public class EmployeeEntity implements Persistable<Long> {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @Column(name = "EMP_NAME")
        private String empName;


        @Column(nullable = false, name = "EMAIL_ID", unique = true)
        private String empEmailId;

        @Column(name = "MOBILE_NUMBER")
        private String mobileNumber;

        @Column(nullable = false, name = "ROLES")
        private String empRoles;

        @Column(name = "SALARY")
        private String  salary;

        @Transient
        private boolean isNew = true;
    }
