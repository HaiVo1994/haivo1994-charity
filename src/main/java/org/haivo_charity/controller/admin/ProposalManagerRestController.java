package org.haivo_charity.controller.admin;

import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.haivo_charity.service.CategoryService;
import org.haivo_charity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/proposal_admin")
public class ProposalManagerRestController {
    @Autowired
    private VoteService voteService;
    @RequestMapping(value = "/getAllProposal/{page}/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Vote>> getListProposal(
            @PathVariable("page") int page, @PathVariable("size") int size){
        List<Vote> votes = voteService.getListProposal(page,size);
        if (votes.size()==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }

    @RequestMapping(value = "/getProposal/{id}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Vote> getProposal(
            @PathVariable("id") long id){
        Vote vote = voteService.findById(id);
        if (vote.isDeleted()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(vote.isAccepted()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(vote, HttpStatus.OK);
    }
    @RequestMapping(value = "/getProposalPage/{size}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Integer> getProposal(
            @PathVariable("size") int size){
        return new ResponseEntity<>(voteService.getAmountPageProposal(size), HttpStatus.OK);
    }

    @RequestMapping(value = "/acceptProposal/{id}",method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> acceptProposal(Principal principal,
                                                 @PathVariable("id") long id){
        if (voteService.acceptProposal(id,principal.getName())){
            return new ResponseEntity<>("Bạn Đã Chấp Nhận Đề Nghị", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Autowired
    private CategoryService categoryService;
    @RequestMapping(value = "/listCategory", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HashMap<Long, String>> getListCategory(){
        Iterable<Category> categories = categoryService.findAll();
        HashMap<Long, String> list = new HashMap<>();
        long count = 0;
        for(Category category: categories){
            count = categoryService.countVoteVyCategory(category);
            list.put(category.getId(), category.getName() + " (" + count + ")");
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
