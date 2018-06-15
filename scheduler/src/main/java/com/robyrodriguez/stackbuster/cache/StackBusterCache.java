package com.robyrodriguez.stackbuster.cache;

import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.Question;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StackBusterCache {

    private Map<String, BaseWorkingQuestion> cache = new ConcurrentHashMap<>();

    public Set<Map.Entry<String, BaseWorkingQuestion>> entries() {
        return cache.entrySet();
    }

    public BaseWorkingQuestion get(Question question) {
        BaseWorkingQuestion existing = cache.get(question.getId());
        if (existing == null) {
            cache.put(question.getId(), mock(question.getUser_id()));
            return null;
        }
        existing.setUser_id(question.getUser_id());
        return existing;
    }

    public void set(BaseWorkingQuestion question) {
        BaseWorkingQuestion existing = cache.get(question.getId());
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

    private BaseWorkingQuestion mock(final String user_id) {
        return new BaseWorkingQuestion() {

            @Override
            public int incrementClicks() {
                return -1;
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public void setId(final String pId) {
            }

            @Override
            public String getUser_id() {
                return user_id;
            }

            @Override
            public void setUser_id(final String user_id) {
            }

            @Override
            public BadgeType getBadgeType() {
                return null;
            }

            @Override
            public void accept(final IncrementStrategyVisitor visitor) throws Exception {
                throw new RuntimeException("called increment execution on working question mock");
            }
        };
    }
}
