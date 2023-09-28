package com.example.testassignment.repository;

import com.example.testassignment.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    List<Users> findByBirthdateBetween(LocalDate from, LocalDate to);
}
