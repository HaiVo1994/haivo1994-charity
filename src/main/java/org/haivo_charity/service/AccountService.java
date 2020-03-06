package org.haivo_charity.service;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Vote;

import java.util.List;

public interface AccountService {
    Account findById(Long id);
    Account findByUsername(String username);
    Account update(Account originAccount, String userName, String oldPassWord, String newPassword, String name, String email, String phone);
    String create(String userName, String password, String yourName, String yourEmail, String yourPhone);
//    void deleteAccount(String username);

    long countVolunteer();

    List<Vote> getListYourDonate(Account account, int page, int size);
    int amountPageYourDonate(String userName, int size);
    long amountYouDonatedForVote(Account account, Vote vote);
}
