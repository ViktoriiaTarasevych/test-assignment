package com.example.testassignment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String firstName;
    String lastName;
    String email;
    @Past(message = "Date of birth must be in the past")
    LocalDate birthdate;
    String address;
    int phoneNumber;
    @Min(18)
    @Column(name = "age")
    private Integer age;
    @AssertTrue(message = "Age must be greater than or equal to {ageThreshold}")
    public boolean isAgeValid(int ageThreshold) {
        return age >= ageThreshold;
    }
    @Column(columnDefinition = "bytea", name = "profileImage")
    private byte[] profileImage;
}
