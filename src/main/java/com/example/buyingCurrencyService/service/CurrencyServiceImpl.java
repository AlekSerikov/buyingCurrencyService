package com.example.buyingCurrencyService.service;

import com.example.buyingCurrencyService.dao.AccountRepository;
import com.example.buyingCurrencyService.handlers.exception.NoConnectionWithServiceException;
import com.example.buyingCurrencyService.model.Currency;
import com.example.buyingCurrencyService.model.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CircuitBreakerFactory cbFactory;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${currencyRateServiceBasePartURL}")
    private String currencyRateServiceBasePartURL;


    @Override
    public Account getAccount(String login) {
        return accountRepository.findByLogin(login);
    }

    @Override
    public Currency getParticularUserCurrencyAndCheckIfCurrencyPresent(String login, String currencyName) {
        return getParticularCurrencyFromAccount(accountRepository.findByLogin(login), currencyName)
                .orElseThrow(() -> new IllegalArgumentException("There is no currency with name " + currencyName));
    }

    @Override
    public Account topUpAnAccount(String userName, Currency currency) {
        Account account = accountRepository.findByLogin(userName);
        Currency costOfOneUnitOfCurrencyInBYN = getCostOfOneUnitOfCurrencyInBYN(currency.getName());
        double theCostOfBuyingCurrencyInRubles = currency.getValue() * costOfOneUnitOfCurrencyInBYN.getValue();

        if (theCostOfBuyingCurrencyInRubles > account.getBalance()) {
            throw new IllegalArgumentException("Not enough money in the balance");
        }

        if (isCurrencyPresentsInAccount(account, currency.getName())) {
            account.getCurrencies().add(new Currency(currency.getName(), 0.0));
            accountRepository.save(account);
        }
        Currency currencyToUpdate = getParticularCurrencyFromAccount(account, currency.getName()).get();
        account.setBalance(account.getBalance() - theCostOfBuyingCurrencyInRubles);
        currencyToUpdate.setValue(currencyToUpdate.getValue() + currency.getValue());
        return accountRepository.save(account);
    }

    private Currency getCostOfOneUnitOfCurrencyInBYN(String currencyName) {
        String costOfOneUnitOfCurrencyInBynAsJson = cbFactory.create("slow").run(()
                -> restTemplate.getForObject(currencyRateServiceBasePartURL + currencyName, String.class), throwable -> "fallback");
        try {
            if (costOfOneUnitOfCurrencyInBynAsJson.equals("fallback")) throw new NoConnectionWithServiceException();
            return objectMapper.readValue(costOfOneUnitOfCurrencyInBynAsJson, Currency.class);
        } catch (JsonProcessingException | NoConnectionWithServiceException e) {
            throw new IllegalArgumentException("Internal server error");
        }
    }

    private Optional<Currency> getParticularCurrencyFromAccount(Account account, String currencyName) {
        return account.getCurrencies().stream()
                .filter(c -> c.getName().equalsIgnoreCase(currencyName))
                .findAny();
    }

    private boolean isCurrencyPresentsInAccount(Account account, String currencyName) {
        return getParticularCurrencyFromAccount(account, currencyName).isEmpty();
    }
}