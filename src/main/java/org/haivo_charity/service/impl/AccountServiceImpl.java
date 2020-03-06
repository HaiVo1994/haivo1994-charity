package org.haivo_charity.service.impl;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Volunteer;
import org.haivo_charity.model.Vote;
import org.haivo_charity.repository.AccountRepository;
import org.haivo_charity.repository.DonateRepository;
import org.haivo_charity.repository.VolunteerRepository;
import org.haivo_charity.repository.VoteRepository;
import org.haivo_charity.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findFirstByUsername(username);
    }

    @Override
    public Account update(Account originAccount, String userName, String oldPassWord,
                       String newPassword, String name, String email, String phone) {
        if (oldPassWord.equals(originAccount.getPassword())){
            Date date = new Date();
            originAccount.setPassword(newPassword);
            originAccount.setUpdated_at(date);
            originAccount.setUpdated_by(userName);
            originAccount = accountRepository.save(originAccount);
            Volunteer volunteer = originAccount.getVolunteer();
            volunteer.setEmail(email);
            volunteer.setName(name);
            volunteer.setPhone(phone);
            volunteer = volunteerRepository.save(volunteer);
            originAccount.setVolunteer(volunteer);
            return originAccount;
        }
        return null;

    }

    @Autowired
    private VolunteerRepository volunteerRepository;
    @Override
    public String create(String userName,String password,String yourName,String yourEmail,String yourPhone) {
        Account checkAccount = accountRepository.findFirstByUsername(userName);
        Volunteer volunteer;
        if(checkAccount!=null){
            return "Tên Tài Khoản Đã Tồn Tại ";
        }
        else{
            if (yourPhone!=null &&
                    !yourPhone.equals("")){
                volunteer = volunteerRepository.
                        findFirstByPhoneOrderByIdDesc(yourPhone);
                if (volunteer!=null) {
                    return "Đã Có Người Dùng Số Điện Thoại Bạn Nhập";
                }
            }
            if (yourEmail!=null &&
                    !yourEmail.equals("")) {
                volunteer = volunteerRepository.
                        findFirstByEmailOrderByIdDesc(yourEmail);
                if (volunteer != null) {
                    return "Đã Có Người Dùng Email Bạn Nhập";
                }
            }

            Date date = new Date();
            Account account = new Account();
//            account.setVolunteer(volunteer);
            account.setUsername(userName);
            account.setPassword(password);
            account.setCreated_at(date);
            account.setRoles("USER");
            account.setEnabled(1);
            account = accountRepository.save(account);

            volunteer = new Volunteer();

            volunteer.setName(yourName);
            volunteer.setPhone(yourPhone);
            volunteer.setEmail(yourEmail);
            volunteer.setCreated_at(date);
            volunteer.setAccount(account);
            volunteerRepository.save(volunteer);
//            volunteer = volunteerRepository.findFirstByPhoneOrderByIdDesc(yourPhone);


            return "";
        }
    }


//    @Override
//    public void deleteAccount(String username) {
//
//    }

    @Override
    public long countVolunteer() {
        return accountRepository.countVolunteer();
    }

    @Autowired
    private DonateRepository donateRepository;
    @Override
    public List<Vote> getListYourDonate(Account account, int page, int size) {
        if (account!=null){
            return donateRepository.getListAccountDonate(account.getVolunteer(), PageRequest.of(page,size)).toList();
        }
        return null;
    }

    @Override
    public int amountPageYourDonate(String userName, int size) {
        Account account = accountRepository.findFirstByUsername(userName);
        if (account!=null){
            int count = donateRepository.getListAccountDonate(account.getVolunteer()).size();
            return (int) Math.ceil((double) count/size);
        }
        return 0;
    }

    @Autowired
    private VoteRepository voteRepository;
    @Override
    public long amountYouDonatedForVote(Account account, Vote vote) {
        if ((account!=null) && (vote!=null)){
            return donateRepository.amountAccountForVote(account.getVolunteer(),vote);
        }
        return 0;
    }
}
