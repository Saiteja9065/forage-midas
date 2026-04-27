package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class IncentiveService {

    private final RestTemplate template;
    private final static String URL = "http://localhost:8080/incentive";

    public float getIncentive(Transaction transaction){
        ResponseEntity<Incentive> response = template.postForEntity(URL, transaction, Incentive.class);

        if (response.getBody() == null) {
            return 0f;
        }

            return response.getBody().getAmount();
    }
}
