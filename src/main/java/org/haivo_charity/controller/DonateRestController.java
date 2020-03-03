package org.haivo_charity.controller;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Donate;
import org.haivo_charity.model.Volunteer;
import org.haivo_charity.model.Vote;
import org.haivo_charity.model.support.RegExp;
import org.haivo_charity.service.AccountService;
import org.haivo_charity.service.DonateService;
import org.haivo_charity.service.VolunteerService;
import org.haivo_charity.service.VoteService;
import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/donateRest")
public class DonateRestController {
    @Autowired
    private DonateService donateService;
    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private VoteService voteService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Iterable<Donate>> getAll(){
        return new ResponseEntity<>(donateService.findAll(), HttpStatus.OK);
    }
    @RequestMapping(value = "/donateAmount/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> getAmountDonated(@PathVariable("id") Long voteId){
        Vote vote = voteService.findById(voteId);
        Long donateTotal = donateService.getTotalDonateOfVote(vote);
        if (donateTotal==null){
            donateTotal = (long) 0;
            return new ResponseEntity<>(donateTotal, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(donateTotal, HttpStatus.OK);
    }

    @RequestMapping(value = "/donateNoLogin/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HashMap<String,String>> donateForVote(@PathVariable("id") Long voteId, @RequestBody JsonObject donateGet){
        Vote vote = voteService.findById(voteId);
        if (vote == null){
            return new ResponseEntity<HashMap<String,String>>(HttpStatus.NOT_FOUND);
        }
        else {
            if ((!vote.isAccepted()) || (vote.getEvent()!=null))
                return new ResponseEntity<HashMap<String,String>>(HttpStatus.BAD_GATEWAY);
        }

        HashMap<String, String> donateResult = new HashMap<String, String>();
        boolean check = true;
        String number = (String) donateGet.get("amount");
        //donateResult.put("amount", number);
        if (!RegExp.checkNumber(number)){
            donateResult.put("errorForMoney", number + " isn't number");
            check = false;
        }

        String name = (String) donateGet.get("name"),
                email = (String) donateGet.get("email");

//                phone = (String) donateGet.get("phone");
//        donateResult.put("name", name);
//        donateResult.put("email", email);
//        donateResult.put("phone", phone);
        int isHided = Integer.parseInt((String) donateGet.get("hide"));
        long amount = 0;

        if (check){
            amount = Long.parseLong(number);
            if (amount<=0){
                donateResult.put("errorForMoney","Lượng Tiền Đóng Góp Phải Lớn Hơn 0!");
                check = false;
            }
        }
//        if ((!email.isEmpty()) && (!RegExp.checkEmail(email))){
//            donateResult.put("errorForEmail", email + " Không Phải Email");
//            check = false;
//        }
//        if ((!phone.isEmpty()) && (!RegExp.checkPhone(phone))){
//            donateResult.put("errorForPhone", phone + " isn't phone");
//            check = false;
//        }

        if (check){
            Donate donate = new Donate();
            donate.setAmount(amount);
            donate.setDate(new Date());
            donate.setVote(vote);
            donate.setHided(isHided == 1);
            donate.setPersonName(name);

            Volunteer volunteer = volunteerService.findByEmail(email);
            if (volunteer == null){
                volunteer = new Volunteer();
                volunteer.setName(name);
                volunteer.setEmail(email);
//                volunteer.setPhone(phone);
                volunteer = volunteerService.save(volunteer);
            }
            donate.setVolunteer(volunteer);
            donateService.save(donate);
            donateResult.put("status", "OK");
            return new ResponseEntity<HashMap<String,String>>(donateResult, HttpStatus.OK);
        }
        else{
            donateResult.put("status", "Fail");
            return new ResponseEntity<HashMap<String,String>>(donateResult, HttpStatus.ACCEPTED);
        }
    }

    @Autowired
    private AccountService accountService;
    @RequestMapping(value = "/donateLogier/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HashMap<String,String>> donateForVoteLogier(
            Principal principal,
            @PathVariable("id") Long voteId,
            @RequestBody JsonObject donateGet){
        Vote vote = voteService.findById(voteId);
        if (vote == null){
            return new ResponseEntity<HashMap<String,String>>(HttpStatus.NOT_FOUND);
        }
        else {
            if ((!vote.isAccepted()) || (vote.getEvent()!=null))
                return new ResponseEntity<HashMap<String,String>>(HttpStatus.BAD_GATEWAY);
        }

        HashMap<String, String> donateResult = new HashMap<String, String>();
        boolean check = true;
        String number = (String) donateGet.get("amount");
        //donateResult.put("amount", number);
        if (!RegExp.checkNumber(number)){
            donateResult.put("errorForMoney", number + " isn't number");
            check = false;
        }
        int isHided = Integer.parseInt((String) donateGet.get("hide"));
        long amount = 0;

        if (check){
            amount = Long.parseLong(number);
            if (amount<=0){
                donateResult.put("errorForMoney","Lượng Tiền Đóng Góp Phải Lớn Hơn 0!");
                check = false;
            }
        }
        Account account = accountService.findByUsername(principal.getName());
        if ((account != null) && (check)){
            Donate donate = new Donate();
            donate.setAmount(amount);
            donate.setDate(new Date());
            donate.setVote(vote);
            donate.setHided(isHided == 1);

            Volunteer volunteer = volunteerService.findByAccount(account);
            donate.setVolunteer(volunteer);
            donate.setPersonName(volunteer.getName());
            donateService.save(donate);
            donateResult.put("status", "OK");
            return new ResponseEntity<HashMap<String,String>>(donateResult, HttpStatus.OK);
        }
        else{
            donateResult.put("status", "Fail");
            return new ResponseEntity<HashMap<String,String>>(donateResult, HttpStatus.ACCEPTED);
        }
    }

    @RequestMapping(value = "/getAmountDonate", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Long> getAmountDonate(){
        Long totalDonate = donateService.getTotalDonate();
        if (totalDonate==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(totalDonate, HttpStatus.OK);
    }

    @RequestMapping(value = "/getListDonateOfVote/{id}/{page}/{size}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Donate>> getAmountDonate(
            @PathVariable long id, @PathVariable int page, @PathVariable int size){
        Vote vote = voteService.findById(id);
        if (vote!=null){
            List<Donate> donates = donateService.getListDonateOfVote(vote,page,size);
            for (Donate donate : donates) {
                if (donate.isHided()) {
                    donate.setPersonName("Ẩn Danh");
                }
                donate.setVolunteer(null);
            }
            return new ResponseEntity<>(donates, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/getAmountPage/{id}/{size}",
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getAmountPage(
            @PathVariable long id, @PathVariable int size){
        Vote vote = voteService.findById(id);
        if (vote!=null){
            return new ResponseEntity<>(donateService.getAmountPage(vote,size), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
