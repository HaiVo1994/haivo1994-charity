package org.haivo_charity.controller;

import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.haivo_charity.service.CategoryService;
import org.haivo_charity.service.EventService;
import org.haivo_charity.service.VoteService;
import com.github.cliftonlabs.json_simple.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/voteRest")
public class VoteRestController {
    @Autowired
    private VoteService voteService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ArrayList<Vote>> getAllVote(){
        ArrayList<Vote> votes = (ArrayList<Vote>) voteService.findAllVoteAccepted();
        if (votes.size()==0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(votes, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Vote> getVote(@PathVariable("id") Long id){
        Vote vote = voteService.findById(id);
        if (vote == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            if ((vote.isAccepted()) && (vote.getEvent() == null))
                return new ResponseEntity<>(vote, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote){
        if (vote==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            vote.setBeginDate(new Date());
            voteService.save(vote);
            return new ResponseEntity<>(vote, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Vote> editVote(@PathVariable("id") Long id, @RequestBody Vote voteEdited){
        Vote vote = voteService.findById(id);
        if (vote == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            vote.setTittle(voteEdited.getTittle());
            vote.setCategories(voteEdited.getCategories());
            vote.setContent(voteEdited.getContent());
            vote.setGoal(voteEdited.getGoal());
            voteService.save(vote);
            return new ResponseEntity<>(vote, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "addTag/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Vote> addTagForVote(@PathVariable("id") Vote vote, @RequestBody JsonObject value){
        if (vote == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            Long idCategory = Long.parseLong((String) value.get("category"));
            Category category = categoryService.findById(idCategory);
            List<Category> categories =(List<Category>)categoryService.findAllByVote(vote);
            if (categories == null){
                categories = new ArrayList<Category>();
            }
            categories.add(category);
            vote.setCategories(categories);
            voteService.save(vote);
            return new ResponseEntity<>(vote, HttpStatus.OK);
        }
    }

//    @RequestMapping
//    public ResponseEntity<Void> removeVote(){
//        return new ResponseEntity<Void>();
//    }

//    @RequestMapping()
//    public ResponseEntity<Void> finish

    @RequestMapping(value = "/getTopVote/{size}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Vote>> getTopVote(@PathVariable("size") int size){
        return new ResponseEntity<>(voteService.getListDateDesc(size), HttpStatus.OK);
    }

    @RequestMapping(value = "/getListPage/{page}/{size}",
            method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Vote>> getListVotePage(@PathVariable("page") int page, @PathVariable("size") int size){
        Pageable pageable = PageRequest.of(page,size);
        return new ResponseEntity<>(voteService.findAllVoteAccepted(pageable), HttpStatus.OK);
    }
    @RequestMapping(value = "/getListPageCategory/{id}/{page}/{size}",
            method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Vote>> getListVotePageCategory(
            @PathVariable("id") Category category, @PathVariable("page") int page, @PathVariable("size") int size){
//        Pageable pageable = PageRequest.of(page,size);
        return new ResponseEntity<>(voteService.findByVotes(category, page, size), HttpStatus.OK);
    }

    @RequestMapping(value = "/getListByTittle/{page}/{size}",
            method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Vote>> getListByTittle(
            @PathVariable("page") int page, @PathVariable("size") int size,
            @RequestBody JsonObject jsonObject){
        String search = (String) jsonObject.get("search");
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(voteService.findByTittle(search,pageable), HttpStatus.OK);
    }

    @RequestMapping(value = "/countByTittle",
            method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> getListByTittle(
            @RequestBody JsonObject jsonObject){
        String search = (String) jsonObject.get("search");
        return new ResponseEntity<>(voteService.countByTittle(search), HttpStatus.OK);
    }

    @Autowired
    private EventService eventService;
    @RequestMapping(value = "/countEventFinish",
            method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> countEventFinish(){
        return new ResponseEntity<>(eventService.countEventFinish(), HttpStatus.OK);
    }
}
