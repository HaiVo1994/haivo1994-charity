package org.haivo_charity.formatter;

import org.haivo_charity.model.Account;
import org.haivo_charity.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class AccountFormatter implements Formatter<Account> {
    private AccountService accountService;

    @Autowired
    public AccountFormatter(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Account parse(String text, Locale locale) throws ParseException {
        return accountService.findById(Long.parseLong(text));
    }

    @Override
    public String print(Account object, Locale locale) {
        return "[" + object.getId() + ", " + object.getUsername() + "]";
    }
}
