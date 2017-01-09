package com.rewards.web;

import com.rewards.model.Transfer;
import com.rewards.model.User;
import com.rewards.model.exception.InvalidPointsAdjustmentException;
import com.rewards.model.exception.UserAlreadyExistsException;
import com.rewards.model.exception.UserNotFoundException;
import com.rewards.repository.UserRepository;
import com.rewards.service.TransferService;
import com.rewards.service.UserService;
import com.rewards.web.request.CreateTransferRequest;
import com.rewards.web.request.CreateUserRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferService transferService;

    //HttpMessageConverter automatically wired up by Spring Boot; converts incoming request
    //JSON to POJO
    @RequestMapping(method = RequestMethod.POST, value = "/users")
    public User createUser(@RequestBody CreateUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user != null) {
            throw new UserAlreadyExistsException(user.getId(), user.getEmail());
        }
        return userService.createUser(request.getEmail(), request.getFirstName(),
                request.getLastName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}/transfers")
    public Set<Transfer> getTransfersForUser(@PathVariable long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        Set<Transfer> transfers = user.getTransfers();
        return user.getTransfers();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/{userId}/transfers")
    public Transfer createTransferForUser(@PathVariable long userId,
                                   @RequestBody CreateTransferRequest request) {
        if (request.getDelta() == 0) {
            throw InvalidPointsAdjustmentException.zeroPoints();
        }
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        long newUserPointsBalance = userService.calculateAdjustedPoints(user, request.getDelta());
        if (newUserPointsBalance < 0) {
            throw InvalidPointsAdjustmentException.withdrawalExceedsUserPoints(userId,
                    request.getDelta());
        }
        return transferService.createTransferAndUpdateUser(userId, request.getDelta());
    }
}
