package org.haivo_charity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 320)
    private String tittle;
    @Column(length = 600)
    private String summary;
    @Column(columnDefinition="TEXT")
    private String content;
    private long goal;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date beginDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date finishDate;

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
    @ColumnDefault("0")
    @JsonIgnore
    private boolean isAccepted;
    @JsonIgnore
    private String proposal_by;
    @JsonIgnore
    private Date proposal_at;
    private String representative;
    private String localVote;

    @OneToMany(mappedBy = "vote", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Donate> donates;

    @OneToMany(mappedBy = "vote", fetch = FetchType.EAGER)
    private List<VoteImage> voteImages;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Event event;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "tag",
            joinColumns = @JoinColumn(name = "voteId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId")
    )
    private List<Category> categories;

    public Vote() {
        this.beginDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
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

    public List<Donate> getDonates() {
        return donates;
    }

    public void setDonates(List<Donate> donates) {
        this.donates = donates;
    }

    public List<VoteImage> getVoteImages() {
        return voteImages;
    }

    public void setVoteImages(List<VoteImage> voteImages) {
        this.voteImages = voteImages;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getProposal_by() {
        return proposal_by;
    }

    public void setProposal_by(String proposal_by) {
        this.proposal_by = proposal_by;
    }

    public Date getProposal_at() {
        return proposal_at;
    }

    public void setProposal_at(Date proposal_at) {
        this.proposal_at = proposal_at;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getLocalVote() {
        return localVote;
    }

    public void setLocalVote(String localVote) {
        this.localVote = localVote;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
