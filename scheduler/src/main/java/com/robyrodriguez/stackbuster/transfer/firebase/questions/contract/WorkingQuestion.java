package com.robyrodriguez.stackbuster.transfer.firebase.questions.contract;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;

public interface WorkingQuestion extends BaseWorkingQuestion {
    int getCurrentViews();
    void setCurrentViews(int currentViews);
}
