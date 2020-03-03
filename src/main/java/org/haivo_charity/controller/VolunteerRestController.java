package org.haivo_charity.controller;

import org.haivo_charity.model.Volunteer;
import org.haivo_charity.service.AccountService;
import org.haivo_charity.service.DonateService;
import org.haivo_charity.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/volunteerRest")
public class VolunteerRestController {
    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private DonateService donateService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Iterable<Volunteer>> getAllVolunteer(){
        Iterable<Volunteer> volunteers = volunteerService.findAll();
        if (((Collection<?>)volunteers).size() == 0){
            return new ResponseEntity<Iterable<Volunteer>>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<Iterable<Volunteer>>(volunteers, HttpStatus.OK);
        }
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Volunteer> findVolunteer(@PathVariable("id") Volunteer volunteer){
        if (volunteer == null){
            return new ResponseEntity<Volunteer>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<Volunteer>(volunteer, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Void> createVolunteer(@RequestBody Volunteer volunteer){
        if (volunteer == null){
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        else {
            volunteerService.save(volunteer);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Volunteer> editVolunteer(@PathVariable("id") Volunteer volunteerCurrent, @RequestBody Volunteer volunteer){
        if (volunteerCurrent == null || volunteer == null){
            return new ResponseEntity<Volunteer>(HttpStatus.NOT_FOUND);
        }
        else {
            volunteerCurrent.setName(volunteer.getName());
            volunteerCurrent.setEmail(volunteer.getEmail());
            volunteerCurrent.setPhone(volunteer.getPhone());
            volunteerService.save(volunteerCurrent);
            return new ResponseEntity<Volunteer>(volunteerCurrent, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Volunteer> deleteVolunteer(@PathVariable("id") Volunteer volunteer){
        if (volunteer==null){
            return new ResponseEntity<Volunteer>(HttpStatus.NO_CONTENT);
        }
        else {
            volunteerService.remove(volunteer.getId());
            return new ResponseEntity<Volunteer>(volunteer, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getAmountVolunteer", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Long> getAmountVolunteer(){
        Long totalVolunteer = volunteerService.getAmountDonors();
        return new ResponseEntity<Long>(totalVolunteer, HttpStatus.OK);
    }

    @Autowired
    private AccountService accountService;
    @RequestMapping(value = "/getNumberVolunteerRegisterEvent", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Long> getNumberVolunteerRegisterEvent(){
        Long totalVolunteer = accountService.countVolunteer();
        return new ResponseEntity<Long>(totalVolunteer, HttpStatus.OK);
    }
}
