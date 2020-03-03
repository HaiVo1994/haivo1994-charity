package org.haivo_charity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "event", fetch = FetchType.EAGER)
    private Vote vote;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date beginDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date finishDate;
    @ColumnDefault(value = "1")
    private Long numberVolunteer;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date registrationDeadline;
    private String location;
    private String note;

    @ColumnDefault(value = "0")
    private boolean isFinished;
    @ColumnDefault(value = "0")
    private boolean isDeleted;
    @JsonIgnore
    private Date deleted_at;
    @JsonIgnore
    private String deleted_by;
    @JsonIgnore
    private Date created_at;
    @JsonIgnore
    private String created_by;
    @JsonIgnore
    private Date updated_at;
    @JsonIgnore
    private String updated_by;

    @OneToMany(mappedBy = "event",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RegisterEvent> registerEvents;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Long getNumberVolunteer() {
        return numberVolunteer;
    }

    public void setNumberVolunteer(Long numberVolunteer) {
        this.numberVolunteer = numberVolunteer;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getDeleted_by() {
        return deleted_by;
    }

    public void setDeleted_by(String deleted_by) {
        this.deleted_by = deleted_by;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public List<RegisterEvent> getRegisterEvents() {
        return registerEvents;
    }

    public void setRegisterEvents(List<RegisterEvent> registerEvents) {
        this.registerEvents = registerEvents;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
