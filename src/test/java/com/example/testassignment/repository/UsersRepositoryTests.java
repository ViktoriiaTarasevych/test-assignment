package com.example.testassignment.repository;

import com.example.testassignment.model.Users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.assertj.core.api.Assertions;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")

public class UsersRepositoryTests {


    @Autowired
    private UsersRepository usersRepository;

    @Test
    @Rollback
    public void testSaveUser() {

        Users users = Users.builder()
                .firstName("Karl")
                .lastName("Gan")
                .email("ddd@jjg.com")
                .address("st,1")
                .birthdate(LocalDate.of(1990, 2,22))
                .phoneNumber(4444).build();

        Users savedUser = usersRepository.save(users);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    @Rollback
    public void testDeleteUser() {
        Users user = new Users();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        user.setBirthdate(LocalDate.of(1995, 5, 20));
        user.setAddress("456 Elm St");
        user.setPhoneNumber(987654321);

        Users savedUser = usersRepository.save(user);

        usersRepository.deleteById(savedUser.getId());
        assertFalse(usersRepository.existsById(savedUser.getId()));
    }

    @Test
    @Rollback
    public void testFindByBirthdateBetween() {

        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(1995, 12, 31);

        List<Users> usersInRange = usersRepository.findByBirthdateBetween(fromDate, toDate);

        assertNotNull(usersInRange);
        assertEquals(2, usersInRange.size());
    }
}
