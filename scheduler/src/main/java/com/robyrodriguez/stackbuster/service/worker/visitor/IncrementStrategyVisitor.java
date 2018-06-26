package com.robyrodriguez.stackbuster.service.worker.visitor;

import com.robyrodriguez.stackbuster.service.worker.strategy.DefaultQuestionStrategy;
import com.robyrodriguez.stackbuster.service.worker.strategy.UserQuestionStrategy;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.DefaultWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.UserWorkingQuestionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Delegates calls to increment strategy execution to specific implementation (avoiding unnecessary use of instanceof)
 */
@Component
public class IncrementStrategyVisitor {

    @Autowired
    private DefaultQuestionStrategy defaultQuestionStrategy;

    @Autowired
    private UserQuestionStrategy userQuestionStrategy;

    public void visit(DefaultWorkingQuestionDO workingQuestion) throws Exception {
        defaultQuestionStrategy.execute(workingQuestion);
    }

    public void visit(UserWorkingQuestionDO workingQuestion) throws Exception {
        userQuestionStrategy.execute(workingQuestion);
    }

    public void visit(com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.DefaultWorkingQuestionDO workingQuestion)
            throws Exception {
        defaultQuestionStrategy.execute(workingQuestion);
    }

    public void visit(com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.UserWorkingQuestionDO workingQuestion)
            throws Exception {
        userQuestionStrategy.execute(workingQuestion);
    }

    public void visit(com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.WorkingQuestionDO workingQuestion)
            throws Exception {
        if (workingQuestion.getUid() == null) {
            defaultQuestionStrategy.execute(workingQuestion);
        } else {
            userQuestionStrategy.execute(workingQuestion);
        }
    }
}
