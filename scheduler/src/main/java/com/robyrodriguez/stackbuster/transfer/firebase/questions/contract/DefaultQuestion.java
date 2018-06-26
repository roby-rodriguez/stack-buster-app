package com.robyrodriguez.stackbuster.transfer.firebase.questions.contract;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseQuestion;
import com.robyrodriguez.stackbuster.types.ProgressType;

public interface DefaultQuestion extends BaseQuestion {
    ProgressType getProgress();
}
