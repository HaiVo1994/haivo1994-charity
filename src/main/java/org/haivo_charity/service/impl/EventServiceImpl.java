package org.haivo_charity.service.impl;

import org.haivo_charity.model.Event;
import org.haivo_charity.model.RegisterEvent;
import org.haivo_charity.model.Vote;
import org.haivo_charity.model.support.RegExp;
import org.haivo_charity.repository.EventRepository;
import org.haivo_charity.repository.RegisterEventRepository;
import org.haivo_charity.repository.VoteRepository;
import org.haivo_charity.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

public class EventServiceImpl implements EventService {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RegisterEventRepository registerEventRepository;

    @Override
    public Event findByVote(Long voteId) {
        Vote vote = voteRepository.findById(voteId).orElse(null);
        if (vote !=null){
            return eventRepository.findEventByVote(vote);
        }
        return null;
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public boolean createEvent(Event event, Long voteId, String userName) {
        Vote vote = voteRepository.findById(voteId).orElse(null);
        if (vote!=null){
            Date date = new Date();
            if (vote.getFinishDate().after(date)){
                if (vote.getEvent()==null){
                    event.setVote(vote);
                    event.setCreated_at(date);
                    event.setCreated_by(userName);
                    String htmlFix = RegExp.removeHTML(event.getLocation());
                    event.setLocation(htmlFix);
                    htmlFix = RegExp.removeHTML(event.getNote());
                    event.setNote(htmlFix);
                    eventRepository.save(event);
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public Event editEvent(Event event, String userName) {
        Event origin = eventRepository.findById(event.getId()).orElse(null);
        if (origin!=null){
            origin.setBeginDate(event.getBeginDate());
            origin.setFinishDate(event.getFinishDate());
            origin.setRegistrationDeadline(event.getRegistrationDeadline());
            origin.setNumberVolunteer(event.getNumberVolunteer());
            origin.setLocation(
                    RegExp.removeHTML(event.getLocation())
            );
            origin.setNote(
                    RegExp.removeHTML(event.getNote())
            );
            origin.setUpdated_at(new Date());
            origin.setUpdated_by(userName);
            origin = eventRepository.save(origin);
            return origin;
        }
        return null;
    }

    @Override
    public boolean confirmFinish(Long eventId, String userName) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event!=null){
            Iterable<RegisterEvent> registerEvents = registerEventRepository.findAllByEvent(event);
            event.setUpdated_by(userName);
            event.setUpdated_at(new Date());
            event.setFinished(true);
            eventRepository.save(event);
            for (RegisterEvent registerEvent : registerEvents){
                if (registerEvent.isAccept()){
                    registerEvent.setFinish(true);
                    registerEventRepository.save(registerEvent);
                }
                else {
                    registerEventRepository.delete(registerEvent);
                }
            }

        }
        return false;
    }

    @Override
    public List<Event> getAllEvent(int page, int size) {
        return eventRepository.getAllEvent(PageRequest.of(page,size)).toList();
    }

    @Override
    public int countEventPage(int size) {
        int count = eventRepository.countEvent();
        return (int) Math.ceil((double) count/size);
    }

    @Override
    public List<Event> getAllEventMustAcceptRegister(int page, int size) {
        return eventRepository.getAllEventMustAcceptRegister(PageRequest.of(page, size)).toList();
    }

    @Override
    public int countEventMustAcceptRegister(int size) {
        int count = eventRepository.countEventMustAcceptRegister();
        return (int) Math.ceil((double) count/size);
    }

    @Override
    public List<Event> getAllEventMustFinish(int page, int size) {
        return eventRepository.getAllEventMustFinish(PageRequest.of(page, size)).toList();
    }

    @Override
    public int countEventMustFinish(int size) {
        int count = eventRepository.countEventMustFinish();
        return (int) Math.ceil((double) count/size);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getEventByUser(String userName, int page, int size) {
        return eventRepository.getEventByUser(userName, PageRequest.of(page,size)).toList();
    }

    @Override
    public int countEventByUser(String userName, int size) {
        int count = eventRepository.countEventByUser(userName);
        return (int) Math.ceil((double) count/size);
    }

    @Override
    public long countEventFinish() {
        return eventRepository.countEventFinish();
    }

    @Override
    public long countRegisterAccept(Event event) {
        if (event!=null){
            return registerEventRepository.countAcceptedByEvent(event);
        }
        return 0;
    }
}
