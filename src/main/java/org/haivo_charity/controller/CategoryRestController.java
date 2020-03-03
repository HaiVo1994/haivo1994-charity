package org.haivo_charity.controller;

import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.haivo_charity.service.CategoryService;
import org.haivo_charity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/categoryRest")
public class CategoryRestController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private VoteService voteService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Iterable<Category>> getAllCategory(){
        Iterable<Category> categories = categoryService.findAll();
        if (((Collection<?>)categories).size() == 0){
            return new ResponseEntity<Iterable<Category>>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<Iterable<Category>>(categories, HttpStatus.OK);
        }
    }

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
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Category> findCategoryById(@PathVariable("id") Long id){
        Category category = categoryService.findById(id);
        if (category == null){
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<Category>(category, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/categoryOfVote/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<Category>> getCategoryOfVote(@PathVariable("id") Long id){
        Vote vote = voteService.findById(id);
        List<Category> categories = (List<Category>) categoryService.findAllByVote(vote);
        if (categories.size()==0){
            return new ResponseEntity<List<Category>>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/countVote/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> countVote(@PathVariable("id") Category category){
        if (category == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(categoryService.countVoteVyCategory(category) , HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/countAll",method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> countAll(){
        return new ResponseEntity<>(categoryService.countAll() , HttpStatus.OK);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        if (category == null){
            return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
        }
        else {
            categoryService.save(category);
            return new ResponseEntity<Category>(category, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Category> editCategory(@PathVariable("id") Long id, @RequestBody Category categoryEdited){
        Category category = categoryService.findById(id);
        if ((category == null) || (categoryEdited == null)){
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }
        else {
            category.setName(categoryEdited.getName());
            categoryService.save(category);
            return new ResponseEntity<Category>(category, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Category> removeCategory(@RequestBody Category category){
        if (category == null){
            return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
        }
        else {
            categoryService.remove(category.getId());
            return new ResponseEntity<Category>(category, HttpStatus.OK);
        }
    }
}
