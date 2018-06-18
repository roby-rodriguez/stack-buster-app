package com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.Question;
import com.robyrodriguez.stackbuster.types.ProgressType;

/**
 * Question items at `/questions/default`
 */
@IgnoreExtraProperties
public class QuestionDO extends AbstractQuestionDO implements Question {

    private String completed;
    private String user_id;
    private ProgressType progress;

    public QuestionDO() {
    }

    public QuestionDO(QuestionDO userQuestion, int viewsCreated) {
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(final String completed) {
        this.completed = completed;
    }

    @Override
    public String getUser_id() {
        return user_id;
    }

    @Override
    public void setUser_id(final String user_id) {
        this.user_id = user_id;
    }

    @Override
    public ProgressType getProgress() {
        return progress;
    }

    public void setProgress(final ProgressType progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "QuestionDO{" + "completed='" + completed + '\'' + ", user_id='" + user_id + '\'' + ", progress="
                + progress + "} " + super.toString();
    }
}
