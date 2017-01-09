package com.rewards.service;

import com.rewards.model.User;
import com.rewards.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser() {
        userService.createUser("ghops@gmail.com", "Grace", "Hopper");
        User user = userRepository.findByEmail("ghops@gmail.com");
        assertEquals("ghops@gmail.com", user.getEmail());
        assertEquals("Grace", user.getFirstName());
        assertEquals("Hopper", user.getLastName());
        assertEquals(0, user.getPoints());
        assertTrue(user.getTransfers().isEmpty());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createUserWithPreexistingEmail() {
        userService.createUser("ghops@gmail.com", "Grace", "Hopper");
        userService.createUser("ghops@gmail.com", "George", "Hopkins");
    }

    @Test
    public void updatePointsForUser() {
        userService.createUser("theprincess@alderaan.gov", "Leia", "Organa");
        User user = userRepository.findByEmail("theprincess@alderaan.gov");
        assertEquals(0, user.getPoints());
        userService.updatePointsForUser(user, 10);
        user = userRepository.findByEmail("theprincess@alderaan.gov");
        assertEquals(10, user.getPoints());
    }
}
