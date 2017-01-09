package com.rewards.service;

import com.rewards.model.Transfer;
import com.rewards.model.User;
import com.rewards.repository.TransferRepository;
import com.rewards.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public Transfer createTransferAndUpdateUser(long userId, long delta) {
        User user = userRepository.findOne(userId);
        userService.updatePointsForUser(user, delta);
        return createTransfer(user, delta);
    }

    private Transfer createTransfer(User user, long delta) {
        Transfer transfer = new Transfer(user, delta);
        transferRepository.save(transfer);
        return transfer;
    }
}
