package com.robyrodriguez.stackbuster.service.worker.dispatcher;

import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncrementDispatcher {

    @Autowired
    private IncrementStrategyVisitor incrementStrategyVisitor;

    public void dispatch(BaseWorkingQuestion question) throws Exception {
        question.accept(incrementStrategyVisitor);
    }
}
