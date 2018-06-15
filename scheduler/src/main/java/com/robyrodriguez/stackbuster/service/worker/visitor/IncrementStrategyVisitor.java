package com.robyrodriguez.stackbuster.service.worker.visitor;

import com.robyrodriguez.stackbuster.service.worker.strategy.QuestionStrategy;
import com.robyrodriguez.stackbuster.service.worker.strategy.UserQuestionStrategy;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.UserWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.WorkingQuestionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncrementStrategyVisitor {

    @Autowired
    private QuestionStrategy questionStrategy;

    @Autowired
    private UserQuestionStrategy userQuestionStrategy;

    public void visit(WorkingQuestionDO workingQuestion) throws Exception {
        questionStrategy.execute(workingQuestion);
    }

    public void visit(UserWorkingQuestionDO workingQuestion) throws Exception {
        userQuestionStrategy.execute(workingQuestion);
    }

    public void visit(com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.WorkingQuestionDO workingQuestion)
            throws Exception {
        questionStrategy.execute(workingQuestion);
    }

    public void visit(com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.UserWorkingQuestionDO workingQuestion)
            throws Exception {
        userQuestionStrategy.execute(workingQuestion);
    }

    public void visit(com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.WorkingQuestionDO workingQuestion)
            throws Exception {
        // TODO aici e un pic mai dubash - de verificat daca are uid - daca da atunci assume user question, alfel
        // default
    }
}
