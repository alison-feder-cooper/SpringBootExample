package com.rewards.service;

import com.rewards.model.Transfer;
import com.rewards.model.User;
import com.rewards.repository.TransferRepository;
import com.rewards.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TransferServiceTest {

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransferService transferService;

    @Before
    public void setup() {
        userRepository.deleteAll();
        transferRepository.deleteAll();
    }

    @Test
    public void createTransferAndUpdateUser() {
        userRepository.save(new User("solo@falcon.biz", "Han", "Solo"));
        User user = userRepository.findByEmail("solo@falcon.biz");
        assertEquals(0, user.getPoints());

        transferService.createTransferAndUpdateUser(user.getId(), 15);

        Transfer transfer = transferRepository.findOne(1L);
        assertEquals(user, transfer.getUser());
        assertEquals(15, transfer.getDelta());
    }
}
