package com.example.testassignment.service;

import com.example.testassignment.exception.AgeRestrictionException;
import com.example.testassignment.model.Users;
import com.example.testassignment.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsersService {


    @Value("${users.age.threshold}")
    private int ageThreshold;

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public void setAgeThreshold(int ageThreshold) {
        this.ageThreshold = ageThreshold;
    }

    private int calculateAge(LocalDate birthdate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthdate, currentDate).getYears();
    }

    public void createUser(Users newUsers) {
        LocalDate birthdate = newUsers.getBirthdate();

        int age = calculateAge(birthdate);

        newUsers.setAge(age);

        if (!newUsers.isAgeValid(ageThreshold)) {
            throw new AgeRestrictionException("You do not meet the age requirement.");
        }
            usersRepository.save(newUsers);
    }


    public Users getUserById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No user found"));
    }

    public void deleteUser(Long id) {
        Users users = getUserById(id);
        usersRepository.delete(users);
    }

    public void updateUser(Long id, Users updatedUsers) {
        Users existingUsers = usersRepository.findById(id).orElse(null);
        if (existingUsers != null) {
            existingUsers.setFirstName(updatedUsers.getFirstName());
            existingUsers.setLastName(updatedUsers.getLastName());
            existingUsers.setEmail(updatedUsers.getEmail());
            existingUsers.setBirthdate(updatedUsers.getBirthdate());
            existingUsers.setAddress(updatedUsers.getAddress());
            existingUsers.setPhoneNumber(updatedUsers.getPhoneNumber());
            usersRepository.save(existingUsers);
        }
    }

    public void partiallyUpdateUser(Long id, Users updatedUsers) {
        Users existingUsers = usersRepository.findById(id).orElse(null);
        if (existingUsers != null) {
            if (updatedUsers.getFirstName() != null) {
                existingUsers.setFirstName(updatedUsers.getFirstName());
            }
            if (updatedUsers.getLastName() != null) {
                existingUsers.setLastName(updatedUsers.getLastName());
            }
            if (updatedUsers.getEmail() != null) {
                existingUsers.setEmail(updatedUsers.getEmail());
            }
            if (updatedUsers.getBirthdate() != null) {
                existingUsers.setBirthdate(updatedUsers.getBirthdate());
            }
            if (updatedUsers.getAddress() != null) {
                existingUsers.setAddress(updatedUsers.getAddress());
            }
            if (updatedUsers.getPhoneNumber() != 0) {
                existingUsers.setPhoneNumber(updatedUsers.getPhoneNumber());
            }
            usersRepository.save(existingUsers);
        }
    }

    public List<Users> getUsersByBirthdateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'From' date should be less than 'To' date");
        }
        return usersRepository.findByBirthdateBetween(from, to);
    }
}
