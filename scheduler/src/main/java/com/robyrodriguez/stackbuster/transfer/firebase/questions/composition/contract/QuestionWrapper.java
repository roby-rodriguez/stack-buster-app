package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.contract;

public interface QuestionWrapper<T> {
    T getT();
    void setT(T t);
}
