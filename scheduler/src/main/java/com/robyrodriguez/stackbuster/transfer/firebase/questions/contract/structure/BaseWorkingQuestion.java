package com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure;

import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitable;

public interface BaseWorkingQuestion extends BaseQuestion, IncrementStrategyVisitable {
    int incrementClicks();
}
