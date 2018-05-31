package com.robyrodriguez.stackbuster.service.listener;

import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.transfer.firebase.UserWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.WorkingQuestionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenerConfig {

    @Autowired
    private StackBusterCache cache;

    @Bean
    public WorkingQuestionsListener workingQuestionsListener() {
        return new WorkingQuestionsListener<>(cache, WorkingQuestionDO.class);
    }

    @Bean
    public WorkingQuestionsListener userWorkingQuestionsListener() {
        return new WorkingQuestionsListener<>(cache, UserWorkingQuestionDO.class);
    }
}
