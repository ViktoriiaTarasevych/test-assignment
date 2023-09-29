package com.example.testassignment.service;

import com.example.testassignment.exception.AgeRestrictionException;
import com.example.testassignment.model.Users;
import com.example.testassignment.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTests {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    @Test
    public void testCreateUserWithValidAge() {
        Users newUser = new Users();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("john.doe@example.com");
        newUser.setBirthdate(LocalDate.of(1990, 1, 15));
        newUser.setAddress("123 Main St");
        newUser.setPhoneNumber(1234567890);

        UsersService usersService = new UsersService(usersRepository);
        usersService.setAgeThreshold(20);

        usersService.createUser(newUser);

        verify(usersRepository, times(1)).save(newUser);
    }

    @Test
    public void testCreateUserWithInvalidAge() {
        Users newUser = new Users();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("john.doe@example.com");
        newUser.setBirthdate(LocalDate.of(2005, 1, 15));
        newUser.setAddress("123 Main St");
        newUser.setPhoneNumber(1234567890);

        UsersService usersService = new UsersService(usersRepository);
        usersService.setAgeThreshold(30);

        assertThrows(AgeRestrictionException.class, () -> usersService.createUser(newUser));
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        Users user = new Users();
        user.setId(userId);
        user.setFirstName("Ben");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        Users retrievedUser = usersService.getUserById(userId);

        assertEquals(user, retrievedUser);
    }

    @Test
    public void testGetUserByInvalidId() {
        Long userId = 1L;

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> usersService.getUserById(userId));
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        Users userToDelete = new Users();
        userToDelete.setId(userId);

        when(usersRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        assertDoesNotThrow(() -> usersService.deleteUser(userId));
        verify(usersRepository, times(1)).delete(userToDelete);
    }
    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        Users existingUser = new Users();
        existingUser.setId(userId);

        Users updatedUser = new Users();
        updatedUser.setId(userId);
        updatedUser.setFirstName("Updated");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        usersService.updateUser(userId, updatedUser);

        assertEquals("Updated", existingUser.getFirstName());
        verify(usersRepository, times(1)).save(existingUser);
    }

    @Test
    public void testPartiallyUpdateUser() {
        Long userId = 1L;
        Users existingUser = new Users();
        existingUser.setId(userId);

        Users updatedUser = new Users();
        updatedUser.setId(userId);
        updatedUser.setLastName("Updated");

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        usersService.partiallyUpdateUser(userId, updatedUser);

        assertEquals("Updated", existingUser.getLastName());
        verify(usersRepository, times(1)).save(existingUser);
    }
}
