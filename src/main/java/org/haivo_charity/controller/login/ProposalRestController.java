package org.haivo_charity.controller.login;

import org.haivo_charity.model.Vote;
import org.haivo_charity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/proposalRest")
public class ProposalRestController {
    @Autowired
    private VoteService voteService;
    @RequestMapping(value = "/getAllProposalUser/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Vote>> getListProposal(Principal principal,
                                                      @PathVariable("page") int page, @PathVariable("size") int size){
        List<Vote> votes = voteService.getListProposalUser(principal.getName(),page,size);
        if (votes.size()==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }

    @RequestMapping(value = "/getProposalUser/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getProposal(Principal principal,
                                               @PathVariable("size") int size){
        return new ResponseEntity<>(voteService.getAmountPageUserProposal(principal.getName(),size), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllVoteUser/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Vote>> getListVote(Principal principal,
                                                  @PathVariable("page") int page, @PathVariable("size") int size){
        List<Vote> votes = voteService.getListVoteOfUser(principal.getName(),page,size);
        if (votes.size()==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }

    @RequestMapping(value = "/getVoteUser/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getVoteUserPage(Principal principal,
                                                   @PathVariable("size") int size){
        return new ResponseEntity<>(voteService.getAmountPageUserVote(principal.getName(),size), HttpStatus.OK);
    }
}
