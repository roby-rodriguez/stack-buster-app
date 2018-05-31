package com.robyrodriguez.stackbuster.service.worker.dispatcher;

import com.robyrodriguez.stackbuster.service.worker.strategy.IncrementStrategy;
import com.robyrodriguez.stackbuster.service.worker.strategy.QuestionStrategy;
import com.robyrodriguez.stackbuster.service.worker.strategy.UserQuestionStrategy;
import com.robyrodriguez.stackbuster.transfer.firebase.AbstractWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.UserWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.WorkingQuestionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class IncrementDispatcher {

    private Map<Class, IncrementStrategy> dispatcher = new HashMap<>();

    @Autowired
    public IncrementDispatcher(
            QuestionStrategy questionStrategy,
            UserQuestionStrategy userQuestionStrategy
    ) {
        dispatcher.put(WorkingQuestionDO.class, questionStrategy);
        dispatcher.put(UserWorkingQuestionDO.class, userQuestionStrategy);
    }

    @SuppressWarnings("unchecked")
    public void dispatch(AbstractWorkingQuestionDO question) throws Exception {
        IncrementStrategy strategy = dispatcher.get(question.getClass());
        if (strategy == null) {
            throw new Exception("Incrementation strategy not found for DO class type " + question.getClass());
        }
        strategy.execute(question);
    }
}
