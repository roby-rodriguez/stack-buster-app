package com.robyrodriguez.stackbuster.transfer.firebase.others;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;
import com.robyrodriguez.stackbuster.types.ProgressType;

import java.util.Date;

public class HistoryEntryDO<T extends BaseWorkingQuestion> {
    private T data;
    private long ended;
    private ProgressType progress;

    public HistoryEntryDO() {
    }

    public HistoryEntryDO(T data, ProgressType progress) {
        this.data = data;
        this.ended = new Date().getTime();
        this.progress = progress;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public long getEnded() {
        return ended;
    }

    public void setEnded(final long ended) {
        this.ended = ended;
    }

    public ProgressType getProgress() {
        return progress;
    }

    public void setProgress(final ProgressType progress) {
        this.progress = progress;
    }
}
