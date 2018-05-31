package com.robyrodriguez.stackbuster.transfer.stack_api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StackQuestionDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> tags;
    private StackUserDO owner;
    private boolean is_answered;
    private int view_count;
    private long accepted_answer_id;
    private int answer_count;
    private int score;
    private long last_activity_date;
    private long creation_date;
    private long last_edit_date;
    private long question_id;
    private String link;
    private String title;

    public StackQuestionDO() {
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(final List<String> pTags) {
        tags = pTags;
    }

    public StackUserDO getOwner() {
        return this.owner;
    }

    public void setOwner(final StackUserDO pOwner) {
        owner = pOwner;
    }

    public boolean is_answered() {
        return this.is_answered;
    }

    public void setIs_answered(final boolean pIs_answered) {
        is_answered = pIs_answered;
    }

    public int getView_count() {
        return this.view_count;
    }

    public void setView_count(final int pView_count) {
        view_count = pView_count;
    }

    public long getAccepted_answer_id() {
        return this.accepted_answer_id;
    }

    public void setAccepted_answer_id(final long pAccepted_answer_id) {
        accepted_answer_id = pAccepted_answer_id;
    }

    public int getAnswer_count() {
        return this.answer_count;
    }

    public void setAnswer_count(final int pAnswer_count) {
        answer_count = pAnswer_count;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(final int pScore) {
        score = pScore;
    }

    public long getLast_activity_date() {
        return this.last_activity_date;
    }

    public void setLast_activity_date(final long pLast_activity_date) {
        last_activity_date = pLast_activity_date;
    }

    public long getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(final long pCreation_date) {
        creation_date = pCreation_date;
    }

    public long getLast_edit_date() {
        return this.last_edit_date;
    }

    public void setLast_edit_date(final long pLast_edit_date) {
        last_edit_date = pLast_edit_date;
    }

    public long getQuestion_id() {
        return this.question_id;
    }

    public void setQuestion_id(final long pQuestion_id) {
        question_id = pQuestion_id;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(final String pLink) {
        link = pLink;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String pTitle) {
        title = pTitle;
    }

    @Override
    public String toString() {
        return "StackQuestionDO{" + "tags=" + tags + ", owner=" + owner + ", is_answered=" + is_answered
                + ", view_count=" + view_count + ", accepted_answer_id=" + accepted_answer_id + ", answer_count="
                + answer_count + ", score=" + score + ", last_activity_date=" + last_activity_date + ", creation_date="
                + creation_date + ", last_edit_date=" + last_edit_date + ", question_id=" + question_id + ", link='"
                + link + '\'' + ", title='" + title + '\'' + '}';
    }
}
