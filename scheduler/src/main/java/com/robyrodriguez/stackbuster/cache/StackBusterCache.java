package com.robyrodriguez.stackbuster.cache;

import com.robyrodriguez.stackbuster.transfer.firebase.AbstractWorkingQuestionDO;
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

    public AbstractWorkingQuestionDO get(String id) {
        return cache.get(id);
    }

    public void set(AbstractWorkingQuestionDO question) {
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
