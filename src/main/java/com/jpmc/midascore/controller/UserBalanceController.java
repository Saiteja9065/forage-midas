package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserBalanceController {

    private final UserRepository userRepository;

    @GetMapping(value = "/balance", produces = "application/json")
    public Balance getbalance(@RequestParam Long userId){
        Optional<UserRecord> userRecord = userRepository.findById(userId);

        if(userRecord.isEmpty()){
            return new Balance(0f);
        }
        return new Balance(userRecord.get().getBalance());
    }
}
