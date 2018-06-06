package com.robyrodriguez.stackbuster.transfer.firebase;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.types.ProgressType;

/**
 * Question items at `/questions`
 */
@IgnoreExtraProperties
public class QuestionDO extends AbstractQuestionDO {

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(final String user_id) {
        this.user_id = user_id;
    }

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
