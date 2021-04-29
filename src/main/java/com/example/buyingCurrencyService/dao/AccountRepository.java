package com.example.buyingCurrencyService.dao;

import com.example.buyingCurrencyService.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByLogin(String login);
}