package org.haivo_charity.controller.login;

import org.haivo_charity.model.Account;
import org.haivo_charity.model.Vote;
import org.haivo_charity.service.AccountService;
import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/donated")
public class DonatedRestController {
    @Autowired
    private AccountService accountService;
    @RequestMapping(value = "/listVote/{page}/{size}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<JsonObject>> listVoteDonated(Principal principal, @PathVariable int page, @PathVariable int size){
        Account account = accountService.findByUsername(principal.getName());
        List<Vote> votes = accountService.getListYourDonate(account, page, size);
        if (votes.size()>0){
            List<JsonObject> listResult = new ArrayList<>();
            String link = "";
            for (Vote vote: votes){
                JsonObject jsonObject = new JsonObject();

                if (vote.getEvent()==null){
                    link = "/vote/" + vote.getId();
                }
                else {
                    if (vote.getEvent().isFinished())
                        link = "";
                    else
                        link = "/event/single_event/" + vote.getEvent().getId();
                }
                jsonObject.put("link", link);
                jsonObject.put("vote", vote);
                jsonObject.put("amountDonated", accountService.amountYouDonatedForVote(account,vote));
                listResult.add(jsonObject);
            }
            return new ResponseEntity<>(listResult, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/listVote/{size}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Integer> amountPageVoteDonated(Principal principal, @PathVariable int size){
        int amount =  accountService.amountPageYourDonate(principal.getName(),size);
        return new ResponseEntity<>(amount, HttpStatus.OK);
    }
}
