package com.robyrodriguez.stackbuster.transfer.firebase.questions.contract;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;

import java.util.List;

public interface UserWorkingQuestion extends BaseWorkingQuestion {
    List<String> getAddresses();
    String getUid();
    int getClicks();
}
