package org.haivo_charity.service;

import org.haivo_charity.model.Event;

import java.util.List;

public interface EventService {
    Event findByVote(Long voteId);
    Event findById(Long id);
    boolean createEvent(Event event, Long voteId, String userName);
    Event editEvent(Event event, String userName);
    boolean confirmFinish(Long eventId, String userName);

    List<Event> getAllEvent(int page, int size);
    int countEventPage(int size);
    List<Event> getAllEventMustAcceptRegister(int page, int size);
    int countEventMustAcceptRegister(int size);
    List<Event> getAllEventMustFinish(int page, int size);
    int countEventMustFinish(int size);

    Event save(Event event);

    List<Event> getEventByUser(String userName, int page, int size);
    int countEventByUser(String userName, int size);

    long countEventFinish();
    long countRegisterAccept(Event event);
}
