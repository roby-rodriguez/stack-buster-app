package com.robyrodriguez.stackbuster.cache;

import com.robyrodriguez.stackbuster.transfer.firebase.AbstractWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.QuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.WorkingQuestionDO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StackBusterCache {

    private Map<String, AbstractWorkingQuestionDO> cache = new ConcurrentHashMap<>();

    public Set<Map.Entry<String, AbstractWorkingQuestionDO>> entries() {
        return cache.entrySet();
    }

    public AbstractWorkingQuestionDO get(QuestionDO question) {
        AbstractWorkingQuestionDO existing = cache.get(question.getId());
        if (existing == null) {
            existing = new WorkingQuestionDO();
            existing.setUser_id(question.getUser_id());
            cache.put(question.getId(), existing);
            return null;
        }
        existing.setUser_id(question.getUser_id());
        return existing;
    }

    public void set(AbstractWorkingQuestionDO question) {
        AbstractWorkingQuestionDO existing = cache.get(question.getId());
        if (existing != null) {
            question.setUser_id(existing.getUser_id());
        }
        cache.put(question.getId(), question);
    }

    public void delete(String id) {
        cache.remove(id);
    }

    @Override
    public String toString() {
        return cache.toString();
    }
}
