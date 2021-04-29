package com.example.buyingCurrencyService;

import com.example.buyingCurrencyService.dao.AccountRepository;
import com.example.buyingCurrencyService.model.Currency;
import com.example.buyingCurrencyService.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BuyingCurrencyServiceApplication implements CommandLineRunner {

    @Autowired
    AccountRepository accountRepository;

    public static void main(String[] args) {
        SpringApplication.run(BuyingCurrencyServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        accountRepository.deleteAll();

        accountRepository.save(new Account("admin", 1000, List.of(
                new Currency("USD", 0),
                new Currency("EUR", 150),
                new Currency("AED", 150)
        )));
        accountRepository.save(new Account("user", 2000, List.of(
                new Currency("USD", 150),
                new Currency("EUR", 160)
        )));
    }
}