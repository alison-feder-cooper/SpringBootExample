package com.rewards.service;

import com.rewards.model.User;
import com.rewards.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String email, String firstName, String lastName) {
        //TODO string validation on email, first name, last name
        User user = new User(email, firstName, lastName);
        userRepository.save(user);
        return user;
    }

    public void updatePointsForUser(User user, long pointsDelta) {
        long updatedPointsAmount = calculateAdjustedPoints(user, pointsDelta);
        user.setPoints(updatedPointsAmount);
        userRepository.save(user);
    }

    public long calculateAdjustedPoints(User user, long pointsDelta) {
        return user.getPoints() + pointsDelta;
    }
}
