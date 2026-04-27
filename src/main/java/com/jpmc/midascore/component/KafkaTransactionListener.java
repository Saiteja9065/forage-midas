package com.jpmc.midascore.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.IncentiveService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KafkaTransactionListener {

    private final ObjectMapper mapper;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private float lastWaldorfBalance = Float.MIN_VALUE;
    private final IncentiveService incentiveService;


    @KafkaListener(topics = "${general.kafka-topic}", groupId="midas-group")
    @Transactional
    public void listener(String message) {
        try {
            Transaction transaction = mapper.readValue(message, Transaction.class);
            System.out.println("Listener hit RAW: " + message);
            System.out.println("Received: " + transaction.getAmount()+"-"+transaction.getSenderId()+"-"+transaction.getRecipientId());
            UserRecord senderRecord = userRepository.findById(transaction.getSenderId()).orElse(null);
            UserRecord recipientRecord = userRepository.findById(transaction.getRecipientId()).orElse(null);
            float incentive = incentiveService.getIncentive(transaction);
            if(senderRecord == null || recipientRecord == null){
                return;
            }
            if(senderRecord.getBalance() < transaction.getAmount()){
                return;
            }

            senderRecord.setBalance(senderRecord.getBalance()-transaction.getAmount());
            recipientRecord.setBalance(recipientRecord.getBalance()+transaction.getAmount()+incentive);

            userRepository.save(senderRecord);
            userRepository.save(recipientRecord);

            TransactionRecord transactionRecord = new TransactionRecord(transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount());
            transactionRepository.save(transactionRecord);
            if ("waldorf".equals(senderRecord.getName()) || "waldorf".equals(recipientRecord.getName())) {
                Optional<UserRecord> waldorf = userRepository.findByName("waldorf");


                if (waldorf.isPresent()) {
                    float curBalance = waldorf.get().getBalance();
                    if(curBalance!=waldorf.get().getBalance()){
                        System.out.println("WALDORF UPDATE → " + curBalance);
                        lastWaldorfBalance = curBalance;
                    }
                    System.out.println("WALDORF UPDATE → " + waldorf.get().getBalance());
                }
            }

            if ("wilbur".equals(senderRecord.getName()) || "wilbur".equals(recipientRecord.getName())) {
                Optional<UserRecord> waldorf = userRepository.findByName("wilbur");


                if (waldorf.isPresent()) {
                    float curBalance = waldorf.get().getBalance();
                    if(curBalance!=waldorf.get().getBalance()){
                        System.out.println("WILBUR UPDATE → " + curBalance);
                        lastWaldorfBalance = curBalance;
                    }
                    System.out.println("WILBUR UPDATE → " + waldorf.get().getBalance());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
