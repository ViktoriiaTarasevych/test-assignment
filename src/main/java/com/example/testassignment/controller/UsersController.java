package com.example.testassignment.controller;

import com.example.testassignment.exception.ErrorResponse;
import com.example.testassignment.model.Users;
import com.example.testassignment.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class UsersController {


    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody Users users) {
        try {

            String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(users.getEmail());

            if (!matcher.matches()) {
                return ResponseEntity.badRequest().body("Invalid email format");
            }

            usersService.createUser(users);
            return ResponseEntity.ok("User successfully created");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create user: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/profileImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long userId,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            usersService.uploadProfileImage(userId, file);
            return ResponseEntity.ok("Profile image uploaded successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload profile image: " + e.getMessage());
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<String> partiallyUpdateUser(@PathVariable Long id, @RequestBody Users updateUsers) {
        try {
            usersService.partiallyUpdateUser(id, updateUsers);
            return ResponseEntity.ok("User successfully updated");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody Users updateUsers) {
        try {
            usersService.updateUser(id, updateUsers);
            return ResponseEntity.ok("User successfully updated");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            usersService.deleteUser(id);
            return ResponseEntity.ok("User successfully deleted");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }


    @GetMapping("/users")    // users?from=2023-09-10&to=2023-09-20
    public ResponseEntity<?> getUser(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from.isAfter(to)) {
            ErrorResponse errorResponse = new ErrorResponse("'From' date should be less than 'To' date");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            List<Users> users = usersService.getUsersByBirthdateRange(from, to);

            if (users.isEmpty()) {
                return ResponseEntity.ok("No users found in the specified date range");
            }

            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/image")
    public String getImagePage(Model model) {
        List<Users> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        return "image";
    }
}
