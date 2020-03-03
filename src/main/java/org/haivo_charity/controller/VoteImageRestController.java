package org.haivo_charity.controller;

import org.haivo_charity.model.Vote;
import org.haivo_charity.model.VoteImage;
import org.haivo_charity.service.VoteImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/VoteImageRest")
public class VoteImageRestController {
    @Autowired
    private VoteImageService voteImageService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Iterable<VoteImage>> getAll(){
        return new ResponseEntity<>(voteImageService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<VoteImage> addImage(@PathVariable("id") Vote vote, @RequestBody VoteImage voteImage){
        voteImage.setVote(vote);
        voteImageService.save(voteImage);
        return new ResponseEntity<>(voteImage, HttpStatus.CREATED);
    }
//    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
//    public ResponseEntity<VoteImage> editImage(@PathVariable("id") Vote vote, @RequestBody VoteImage voteImage){
//        List<VoteImage> original = vote.getVoteImages();
//        original.setSource(voteImage.getSource());
//        voteImageService.save(original);
//        return new ResponseEntity<>(original, HttpStatus.OK);
//    }
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
//    public ResponseEntity<VoteImage> deleteImage(@PathVariable("id") Vote vote){
//        VoteImage original = vote.getVoteImage();
//        voteImageService.remove(original.getId());
//        return new ResponseEntity<>(original, HttpStatus.OK);
//    }
    @RequestMapping(value = "/getTopImage/{size}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<VoteImage>> getTopImage(@PathVariable int size){
        return new ResponseEntity<>(voteImageService.getTopImage(size), HttpStatus.OK);
    }
}
