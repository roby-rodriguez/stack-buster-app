package com.robyrodriguez.stackbuster.transfer.firebase;

import com.robyrodriguez.stackbuster.types.ProgressType;

import java.util.Date;

public class HistoryEntryDO<T extends AbstractWorkingQuestionDO> {
    private T data;
    private long ended;

    public HistoryEntryDO() {
    }

    public HistoryEntryDO(T data, ProgressType progress) {
        this.data = data;
        this.data.setProgress(progress);
        this.ended = new Date().getTime();
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
}
